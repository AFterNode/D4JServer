package cn.afternode.d4jserver.api.event.events.message;

import cn.afternode.d4jserver.api.event.eventapi.events.Event;
import discord4j.core.event.domain.message.MessageBulkDeleteEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;
import lombok.Getter;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Called by MessageBulkDeleteEvent
 * @see discord4j.core.event.domain.message.MessageBulkDeleteEvent
 */
public class MultiMessageDeleteEvent implements Event {
    @Getter
    private final Guild guild;
    private final MessageBulkDeleteEvent raw;

    @Getter
    private final Set<Message> messages;

    public MultiMessageDeleteEvent(MessageBulkDeleteEvent raw, Guild guild) {
        this.raw = raw;
        this.guild = guild;
        this.messages = raw.getMessages();
    }

    public Mono<TextChannel> getChannel() {
        return raw.getChannel();
    }

    public TextChannel getChannelAwait() {
        return getChannel().block();
    }
}
