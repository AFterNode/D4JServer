package cn.afternode.d4jserver.api.plugin;

import cn.afternode.d4jserver.api.D4JServerAPI;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Plugin {
    @Getter
    private boolean enabled = false;
    @Getter
    private final PluginLogger logger = new PluginLogger(this);

    public void onEnable() {}
    public void onDisable() {}

    public final D4JServerPlugin getMeta() {
        return D4JServerAPI.getServer().getPluginManager().getPluginMeta(this);
    }

    public final void enable() {
        if (enabled) throw new IllegalStateException("Plugin " + getMeta().name() + " already enabled");
        logger.info("Enabling " + getMeta().name() + " v" + getMeta().version());
        onEnable();
        enabled = true;
    }

    public final void disable() {
        if (!enabled) throw new IllegalStateException("Plugin " + getMeta().name() + " not enabled");
        logger.info("Disabling " + getMeta().name() + " v" + getMeta().version());
        onDisable();
        enabled = false;
    }
}
