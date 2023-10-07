package cn.afternode.d4jserver.api.event.events.message;

import cn.afternode.d4jserver.api.event.eventapi.events.Event;
import discord4j.core.event.domain.message.MessageBulkDeleteEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.Getter;

import java.util.Set;

/**
 * Called by MessageBulkDeleteEvent
 * @see discord4j.core.event.domain.message.MessageBulkDeleteEvent
 */
@Getter
public class MultiMessageDeleteEvent implements Event {
    private final Guild guild;
    private final Set<Message> messages;
    private final MessageChannel channel;

    public MultiMessageDeleteEvent(MessageBulkDeleteEvent raw, Guild guild) {
        this.guild = guild;
        this.messages = raw.getMessages();
        this.channel = raw.getChannel().block();
    }
}
