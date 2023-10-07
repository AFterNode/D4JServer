package cn.afternode.d4jserver.api.plugin;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PluginLogger {
    @Getter
    private final Plugin plugin;
    private final Logger logger;

    PluginLogger(Plugin plugin) {
        this.plugin = plugin;
        this.logger = LogManager.getLogger("Plugins/"+plugin.getMeta().name());
    }

    public void info(String message) {
        logger.info(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void warn(String message, Throwable throwable) {
        logger.warn(message, throwable);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
