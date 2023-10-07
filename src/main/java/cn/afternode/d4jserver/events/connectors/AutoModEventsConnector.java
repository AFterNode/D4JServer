package cn.afternode.d4jserver.events.connectors;

import cn.afternode.d4jserver.api.event.events.automod.*;
import cn.afternode.d4jserver.events.SubConnector;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.automod.*;

public class AutoModEventsConnector extends SubConnector {
    private void onActionExecuted(AutoModActionExecutedEvent event) {
        try {
            if (checkGuild(event.getGuildId().asLong())) callConnect(new AMActionExecutedEvent(event));
        } catch (Throwable t) {
            handleError(event, t);
        }
    }

    private void onRuleCreate(AutoModRuleCreateEvent event) {
        try {
            if (checkGuild(event.getGuildId().asLong())) callConnect(new AMRuleCreateEvent(event));
        } catch (Throwable t) {
            handleError(event, t);
        }
    }

    private void onRuleDelete(AutoModRuleDeleteEvent event) {
        try {
            if (checkGuild(event.getGuildId().asLong())) callConnect(new AMRuleDeleteEvent(event));
        } catch (Throwable t) {
            handleError(event, t);
        }
    }

    private void onRule(AutoModRuleEvent e) {
        try {
            if (checkGuild(e.getGuildId().asLong())) callConnect(new AMRuleEvent(e));
        } catch (Throwable t) {
            handleError(e, t);
        }
    }

    private void onRuleUpdate(AutoModRuleUpdateEvent e) {
        try {
            if (checkGuild(e.getGuildId().asLong())) callConnect(new AMRuleUpdateEvent(e));
        } catch (Throwable t) {
            handleError(e, t);
        }
    }

    @Override
    public void connect(GatewayDiscordClient g) {
        g.on(AutoModActionExecutedEvent.class).subscribe(this::onActionExecuted);
        g.on(AutoModRuleCreateEvent.class).subscribe(this::onRuleCreate);
        g.on(AutoModRuleDeleteEvent.class).subscribe(this::onRuleDelete);
        g.on(AutoModRuleEvent.class).subscribe(this::onRule);
        g.on(AutoModRuleUpdateEvent.class).subscribe(this::onRuleUpdate);
    }
}
