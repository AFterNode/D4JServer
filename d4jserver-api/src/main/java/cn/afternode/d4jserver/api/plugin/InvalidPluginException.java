package cn.afternode.d4jserver.api.plugin;

import java.io.File;

public class InvalidPluginException extends RuntimeException {
    public InvalidPluginException(File source, String message) {
        super("Invalid plugin file " + source.getName() + ": " + message);
    }
}
