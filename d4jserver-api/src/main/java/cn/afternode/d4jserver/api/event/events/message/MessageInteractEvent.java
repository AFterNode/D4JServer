package cn.afternode.d4jserver.api.event.events.message;

import cn.afternode.d4jserver.api.event.eventapi.events.Event;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import lombok.Getter;
import reactor.core.publisher.Mono;

/**
 * Called by MessageInteractionEvent
 * @see discord4j.core.event.domain.interaction.MessageInteractionEvent
 */
public final class MessageInteractEvent implements Event {
    private final MessageInteractionEvent raw;

    @Getter
    private final Message resolvedMessage;

    public MessageInteractEvent(MessageInteractionEvent raw, Guild g) {
        this.raw = raw;

        this.resolvedMessage = raw.getResolvedMessage();
    }

    public Guild getGuildAwait() {
        return raw.getInteraction().getGuild().block();
    }

    public Mono<Guild> getGuild() {
        return raw.getInteraction().getGuild();
    }

    public Message getTargetAwait() {
        return raw.getTargetMessage().block();
    }

    public Mono<Message> getTarget() {
        return raw.getTargetMessage();
    }

    public Snowflake getTargetID() {
        return raw.getTargetId();
    }
}
