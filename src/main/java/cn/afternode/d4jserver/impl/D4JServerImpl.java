package cn.afternode.d4jserver.impl;

import cn.afternode.d4jserver.api.D4JServer;
import cn.afternode.d4jserver.api.D4JServerAPI;
import cn.afternode.d4jserver.api.GuildManager;
import cn.afternode.d4jserver.api.command.CommandManager;
import cn.afternode.d4jserver.api.plugin.D4JServerPlugin;
import cn.afternode.d4jserver.api.plugin.Plugin;
import cn.afternode.d4jserver.api.plugin.PluginManager;
import cn.afternode.d4jserver.db.DatabaseManager;
import cn.afternode.d4jserver.events.EventConnector;
import cn.afternode.d4jserver.api.event.eventapi.EventManager;
import cn.afternode.d4jserver.config.D4JServerConfig;
import cn.afternode.d4jserver.plugin.DependencyManager;
import cn.afternode.d4jserver.plugin.PluginManagerImpl;
import cn.afternode.d4jserver.utils.GitUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import discord4j.core.CoreResources;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class D4JServerImpl implements D4JServer {
    public static final File configFile = new File("config.json");
    public static final File pluginsDir = new File("plugins");

    @Getter
    private static D4JServerImpl instance;

    @Getter
    private final Logger logger = LogManager.getLogger("Main");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private boolean running = false;
    private boolean looping = false;

    private DiscordClient client;
    private GatewayDiscordClient gatewayClient;
    private EventManager eventManager;
    private GuildManager guildManager;
    private PluginManager pluginManager;
    private CommandManagerImpl commandManager;
    private DependencyManager dependencyManager;
    @Getter
    private D4JServerConfig config;

    private EventConnector eventConnector;
    @Getter
    private DatabaseManager databaseManager;

    public void launch(String[] args) {
        try {
            running = true;
            instance = this;

            logger.info("Starting " + getName());
            logger.info("VM: " + System.getProperty("java.name") + System.getProperty("java.version"));


            // region: Configuration
            logger.info("Loading configuration...");
            try {
                if (!configFile.exists()) {
                    if (!configFile.createNewFile()) throw new IOException("Configuration file not created.");
                    config = new D4JServerConfig();
                    Files.writeString(configFile.toPath(), gson.toJson(config), StandardCharsets.UTF_8);
                } else {
                    config = gson.fromJson(Files.readString(configFile.toPath(), StandardCharsets.UTF_8), D4JServerConfig.class);
                }

                guildManager = new GuildManagerImpl();
            } catch (Throwable t) {
                throw new RuntimeException("Unable to load configuration", t);
            }
            // endregion

            // region: Dependencies
            dependencyManager = new DependencyManager(config.getMaven());
            boolean result = dependencyManager.resolve(D4JServerImpl.class.getClassLoader(),
                    List.of("org.apache.commons:commons-lang3:3.13.0"),
                    "D4JServer");
            if (!result) throw new RuntimeException("Dependencies resolve failed");
            // endregion

            // region: Database
            databaseManager = new DatabaseManager(config.getDatabaseConfig());
            databaseManager.connect();
            databaseManager.init();
            // endregion

            // region: Initialization
            client = DiscordClient.create(config.getToken());
            logger.info("Logging in...");
            gatewayClient = client.login().block();
            logger.info("Logged in successfully");

            eventManager = new EventManager("D4JServer");
            eventConnector = new EventConnector();
            eventConnector.init(gatewayClient);
            logger.info("Events connected");

            commandManager = new CommandManagerImpl();
            commandManager.init();
            eventManager.register(commandManager);
            logger.info("Commands initialized");

            D4JServerAPI.setServer(this);
            // endregion

            // region: Plugin
            logger.info("Loading plugins...");
            if (!pluginsDir.exists()) {
                pluginManager = new PluginManagerImpl();
                if (!pluginsDir.mkdirs()) {
                    logger.error("Unable to create plugins directory, skipping plugin loading step");
                }
            }
            pluginManager = new PluginManagerImpl();
            pluginManager.loadPlugins(pluginsDir);
            // endregion
        } catch (Throwable t) {
            logger.fatal("Unable to launch", t);
            shutdown();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        logger.info("Event loop started");
        while (running) {
            try {
                looping = true;
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {}
        }
        logger.info("Event loop stopped");
    }

    @Override
    public DiscordClient getClient() {
        return client;
    }

    @Override
    public GatewayDiscordClient getGateway() {
        return gatewayClient;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public GuildManager getGuildManager() {
        return guildManager;
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public boolean resolvePluginDependency(ClassLoader loader, List<String> dependencies, D4JServerPlugin meta) {
        return dependencyManager.resolve(loader, dependencies, "Plugin-" + meta.name());
    }

    @Override
    public String getVersion() {
        return GitUtils.getVersion();
    }

    @Override
    public String getName() {
        return "D4JServer " + getVersion() + " (git-" + GitUtils.getBranch() + "-" + GitUtils.getAbbrev() + ") Discord4J " + GitUtils.getD4jVersion();
    }

    @Override
    public void shutdown() {
        running = false;
        logger.info("Waiting for event loop");
        while (looping) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {}
        }

        try {
            logger.info("Shutting down D4JServer");
            pluginManager.shutdown();
        } catch (Throwable t) {
            logger.fatal("Error shutting down", t);
        }

        logger.info("Disconnecting gateway");
        gatewayClient.onDisconnect().block();
        logger.info("Disconnected");
    }
}
