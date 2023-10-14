package cn.afternode.d4jserver.api;

import cn.afternode.d4jserver.api.event.eventapi.EventManager;
import cn.afternode.d4jserver.api.permission.PermissionManager;
import cn.afternode.d4jserver.api.plugin.PluginManager;
import lombok.Getter;
import lombok.Setter;

public class D4JServerAPI {
    @Getter
    @Setter
    private static D4JServer server;

    public static GuildManager getGuildManager() {
        return server.getGuildManager();
    }

    public static EventManager getEventManager() {
        return server.getEventManager();
    }

    public static PermissionManager getPermissionManager() {
        return server.getPermissionManager();
    }

    public static PluginManager getPluginManager() {
        return server.getPluginManager();
    }
}
