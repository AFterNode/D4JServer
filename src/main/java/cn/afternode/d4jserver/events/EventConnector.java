package cn.afternode.d4jserver.events;

import cn.afternode.d4jserver.events.connectors.AutoModEventsConnector;
import cn.afternode.d4jserver.events.connectors.MessageEventsConnector;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventConnector {
    private final Logger logger = LogManager.getLogger("EventConnector");

    public void init(GatewayDiscordClient gateway) {
        SubConnector[] subs = new SubConnector[]{
                new MessageEventsConnector(),
                new AutoModEventsConnector()
        };

        for (SubConnector sc: subs) sc.connect(gateway);
    }

    private void handleError(Event raw, Throwable t) {
        logger.error(raw.getClass().getSimpleName() + " Error", t);
    }
}
