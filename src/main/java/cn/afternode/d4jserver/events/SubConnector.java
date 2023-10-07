package cn.afternode.d4jserver.events;

import cn.afternode.d4jserver.api.D4JServerAPI;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class SubConnector {
    public abstract void connect(GatewayDiscordClient g);

    protected <T extends cn.afternode.d4jserver.api.event.eventapi.events.Event> T callConnect(T event) {
        return (T) D4JServerAPI.getEventManager().call(event);
    }

    protected static final Logger logger = LogManager.getLogger("EventConnector");

    protected void handleError(Event raw, Throwable t) {
        logger.error(raw.getClass().getSimpleName() + " Error", t);
    }

    protected boolean checkGuild(long id) {
        return D4JServerAPI.getGuildManager().isGuildAllowed(id);
    }
}
