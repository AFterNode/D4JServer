package cn.afternode.d4jserver.api.event.events.automod;

import discord4j.core.event.domain.automod.AutoModRuleEvent;

public class AMRuleDeleteEvent extends AMRuleEvent {
    public AMRuleDeleteEvent(AutoModRuleEvent raw) {
        super(raw);
    }
}
