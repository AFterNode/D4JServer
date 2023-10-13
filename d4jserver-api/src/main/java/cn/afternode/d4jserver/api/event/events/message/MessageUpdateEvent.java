package cn.afternode.d4jserver.api.event.events.message;

import cn.afternode.d4jserver.api.event.eventapi.events.callables.EventCancellable;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.Getter;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Set message content back when cancelled
 * @see discord4j.core.event.domain.message.MessageUpdateEvent
 */
public class MessageUpdateEvent extends EventCancellable {
    private final discord4j.core.event.domain.message.MessageUpdateEvent raw;

    @Getter
    private final Guild guild;

    public MessageUpdateEvent(discord4j.core.event.domain.message.MessageUpdateEvent raw, Guild g) {
        this.raw = raw;

        this.guild = g;
    }

    public Mono<Message> getMessage() {
        return raw.getMessage();
    }

    public Message getMessageAwait() {
        return getMessage().block();
    }

    public Optional<Message> getOld() {
        return raw.getOld();
    }

    public Mono<MessageChannel> getChannel() {
        return raw.getChannel();
    }

    public MessageChannel getChannelAwait() {
        return getChannel().block();
    }
}
