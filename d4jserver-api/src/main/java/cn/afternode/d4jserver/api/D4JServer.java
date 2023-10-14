package cn.afternode.d4jserver.api;

import cn.afternode.d4jserver.api.command.CommandManager;
import cn.afternode.d4jserver.api.event.eventapi.EventManager;
import cn.afternode.d4jserver.api.permission.PermissionManager;
import cn.afternode.d4jserver.api.plugin.D4JServerPlugin;
import cn.afternode.d4jserver.api.plugin.PluginManager;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;

import java.util.List;

public interface D4JServer {
    DiscordClient getClient();

    GatewayDiscordClient getGateway();

    EventManager getEventManager();

    GuildManager getGuildManager();

    PluginManager getPluginManager();

    CommandManager getCommandManager();

    PermissionManager getPermissionManager();

    boolean resolvePluginDependency(ClassLoader loader, List<String> dependencies, D4JServerPlugin meta);

    String getVersion();

    String getName();

    void shutdown();
}
