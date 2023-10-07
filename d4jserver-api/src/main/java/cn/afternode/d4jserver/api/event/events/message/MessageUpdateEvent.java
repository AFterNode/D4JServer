package cn.afternode.d4jserver.api.event.events.message;

import cn.afternode.d4jserver.api.event.eventapi.events.callables.EventCancellable;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.Getter;

/**
 * Set message content back when cancelled
 * @see discord4j.core.event.domain.message.MessageUpdateEvent
 */
@Getter
public class MessageUpdateEvent extends EventCancellable {
    private final Guild guild;
    private final Message message;
    private final Message old;
    private final MessageChannel channel;

    public MessageUpdateEvent(discord4j.core.event.domain.message.MessageUpdateEvent raw, Guild g) {
        this.guild = g;
        this.message = raw.getMessage().block();
        this.old = raw.getOld().get();
        this.channel = raw.getChannel().block();
    }
}
