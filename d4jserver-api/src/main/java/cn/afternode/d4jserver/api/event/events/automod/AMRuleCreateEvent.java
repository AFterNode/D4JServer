package cn.afternode.d4jserver.api.event.events.automod;

import cn.afternode.d4jserver.api.event.eventapi.events.Event;
import discord4j.core.event.domain.automod.AutoModRuleCreateEvent;
import discord4j.core.event.domain.automod.AutoModRuleEvent;

public class AMRuleCreateEvent extends AMRuleEvent {
    public AMRuleCreateEvent(AutoModRuleEvent raw) {
        super(raw);
    }
}
