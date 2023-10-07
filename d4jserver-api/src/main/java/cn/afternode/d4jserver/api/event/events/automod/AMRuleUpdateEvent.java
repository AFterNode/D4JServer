package cn.afternode.d4jserver.api.event.events.automod;

import discord4j.core.event.domain.automod.AutoModRuleEvent;

public class AMRuleUpdateEvent extends AMRuleEvent {
    public AMRuleUpdateEvent(AutoModRuleEvent raw) {
        super(raw);
    }
}
