package cn.afternode.d4jserver.api.plugin;

import java.io.File;
import java.io.IOException;

public interface PluginManager {
    Plugin loadPlugin(File source) throws IOException;

    Plugin[] loadPlugins(File dir) throws IOException;

    Plugin getPlugin(String name);

    Plugin[] getPlugins();

    boolean isEnabled(String name);

    void shutdown();

    D4JServerPlugin getPluginMeta(Plugin plugin);
}
