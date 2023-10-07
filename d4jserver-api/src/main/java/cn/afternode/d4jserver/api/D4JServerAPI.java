package cn.afternode.d4jserver.api;

import cn.afternode.d4jserver.api.event.eventapi.EventManager;
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
}
