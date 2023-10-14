package cn.afternode.d4jserver.plugin;

import cn.afternode.d4jserver.api.D4JServerAPI;
import cn.afternode.d4jserver.api.plugin.D4JServerPlugin;
import cn.afternode.d4jserver.api.plugin.InvalidPluginException;
import cn.afternode.d4jserver.api.plugin.Plugin;
import cn.afternode.d4jserver.api.plugin.PluginManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class PluginManagerImpl implements PluginManager {
    private final Logger logger = LogManager.getLogger("PluginManager");
    private final HashMap<String, Plugin> plugins = new HashMap<>();

    @Override
    public Plugin loadPlugin(File source) {
        logger.info("Loading plugin from " + source.getName());

        try {
            // Load classes
            List<Class<?>> classes = new ArrayList<>();
            Class<?> main = null;
            try (JarFile jf = new JarFile(source)) {
                Enumeration<? extends ZipEntry> entries = jf.entries();
                try (URLClassLoader ucl = new URLClassLoader(new URL[]{source.toURI().toURL()}, getClass().getClassLoader())) {
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        String name = entry.getName().split(".class")[0].replace("/", ".");
                        if (entry.isDirectory() || name.endsWith("module-info") || name.startsWith("META-INF")) continue;

                        Class<?> result = ucl.loadClass(name);
                        classes.add(result);
                        if (result.isAnnotationPresent(D4JServerPlugin.class)) {
                            if (main != null) throw new InvalidPluginException(source, "Multiple main classes detected");
                            main = result;
                        }
                    }
                }
            }
            if (main == null) {
                throw new InvalidPluginException(source, "Main class not found");
            }

            // Check
            D4JServerPlugin meta = main.getAnnotation(D4JServerPlugin.class);
            if (plugins.containsKey(meta.name())) throw new InvalidPluginException(source, "Duplicate plugin name " + meta.name() + " from " + source.getName());

            // Load libraries
            if (meta.dependencies().length != 0) {
                logger.info("Resolving dependencies of " + meta.name());
                if (!D4JServerAPI.getServer().resolvePluginDependency(main.getClassLoader(), Arrays.asList(meta.dependencies()), meta)) {
                    throw new RuntimeException("Unable to resolve dependencies");
                }
            }

            // Initialization
            Plugin instance = (Plugin) main.getConstructor().newInstance();
            instance.enable();

            plugins.put(meta.name(), instance);
            return instance;
        } catch (Throwable t) {
            logger.warn("Unable to load plugin from " + source.getName(), t);
            return null;
        }
    }

    @Override
    public Plugin[] loadPlugins(File dir) throws IOException {
        if (!dir.exists() || !dir.isDirectory()) throw new IOException("Plugin folder \"" + dir.getAbsolutePath() + "\" not exists or not a directory.");

        File[] files = dir.listFiles((file) -> file.getName().endsWith(".jar") && file.isFile());
        if (files == null) throw new IOException("Empty plugin folder \"" + dir.getAbsolutePath() + "\"");
        logger.info("Found " + files.length + " jars in folder \"" + dir.getAbsolutePath() + "\"");

        List<Plugin> loaded = new ArrayList<>();
        for (File f: files) {
            Plugin p = loadPlugin(f);
            if (p != null) loaded.add(p);
        }
        logger.info("Loaded " + loaded.size() + " plugins.");
        return loaded.toArray(new Plugin[0]);
    }

    @Override
    public Plugin getPlugin(String name) {
        return plugins.get(name);
    }

    @Override
    public Plugin[] getPlugins() {
        return plugins.values().toArray(new Plugin[0]);
    }

    @Override
    public boolean isEnabled(String name) {
        Plugin p = getPlugin(name);
        return p == null || !p.isEnabled();
    }

    @Override
    public void shutdown() {
        logger.info("Shutting down plugins...");
        for (Plugin p: plugins.values()) {
            if (p.isEnabled()) {
                try {
                    p.disable();
                } catch (Throwable t) {
                    logger.warn("Error while disabling plugin " + p.getMeta().name() + " v" + p.getMeta().version(), t);
                }
            }
        }
    }

    @Override
    public D4JServerPlugin getPluginMeta(Plugin plugin) {
        if (!plugin.getClass().isAnnotationPresent(D4JServerPlugin.class)) throw new IllegalArgumentException(plugin.getClass().getName() + " has no D4JServerPlugin annotation present");

        return plugin.getClass().getAnnotation(D4JServerPlugin.class);
    }
}
