package cn.afternode.d4jserver.api.event.events.automod;

import cn.afternode.d4jserver.api.event.eventapi.events.Event;
import discord4j.core.event.domain.automod.AutoModRuleEvent;
import discord4j.core.object.entity.Guild;

public class AMRuleEvent implements Event {
    private final AutoModRuleEvent raw;

    public AMRuleEvent(AutoModRuleEvent raw) {
        this.raw = raw;
    }

    public Guild getGuild() {
        return raw.getGuild().block();
    }

}
