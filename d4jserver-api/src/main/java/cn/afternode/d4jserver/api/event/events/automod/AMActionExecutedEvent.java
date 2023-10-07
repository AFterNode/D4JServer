package cn.afternode.d4jserver.api.event.events.automod;

import cn.afternode.d4jserver.api.event.eventapi.events.Event;
import discord4j.core.event.domain.automod.AutoModActionExecutedEvent;
import discord4j.core.object.automod.AutoModRule;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.MessageChannel;

/**
 * @see discord4j.core.event.domain.automod.AutoModActionExecutedEvent
 */
public class AMActionExecutedEvent implements Event {
    private final AutoModActionExecutedEvent raw;

    public AMActionExecutedEvent(AutoModActionExecutedEvent raw) {
        this.raw = raw;
    }

    public Guild getGuild() {
        return raw.getGuild().block();
    }

    public AutoModRule getRule() {
        return raw.getAutoModRule().block();
    }

    public AutoModRule.TriggerType getTriggerType() {
        return raw.getTriggerType();
    }

    public Member getMember() {
        return raw.getMember().block();
    }

    public MessageChannel getChannel() {
        return raw.getChannel().block();
    }
}
