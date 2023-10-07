package cn.afternode.d4jserver.api.event.events.message;

import cn.afternode.d4jserver.api.event.eventapi.events.Event;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.Getter;

/**
 * @see discord4j.core.event.domain.message.MessageDeleteEvent
 */
@Getter
public class MessageDeleteEvent implements Event {
    private final Message message;
    private final Guild guild;
    private final MessageChannel channel;

    public MessageDeleteEvent(discord4j.core.event.domain.message.MessageDeleteEvent raw, Guild guild) {
        if (raw.getMessage().isEmpty()) throw new NullPointerException("Message is null");

        message = raw.getMessage().get();
        channel = raw.getChannel().block();
        this.guild = guild;
    }
}
