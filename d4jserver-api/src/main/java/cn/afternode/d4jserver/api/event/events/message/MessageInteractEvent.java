package cn.afternode.d4jserver.api.event.events.message;

import cn.afternode.d4jserver.api.event.eventapi.events.Event;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import lombok.Getter;

/**
 * Called by MessageInteractionEvent
 * @see discord4j.core.event.domain.interaction.MessageInteractionEvent
 */
@Getter
public class MessageInteractEvent implements Event {
    private final Guild guild;
    private final Message resolvedMessage;
    private final Message targetMessage;

    public MessageInteractEvent(MessageInteractionEvent raw, Guild g) {
        this.guild = g;
        this.resolvedMessage = raw.getResolvedMessage();
        this.targetMessage = raw.getTargetMessage().block();
    }
}
