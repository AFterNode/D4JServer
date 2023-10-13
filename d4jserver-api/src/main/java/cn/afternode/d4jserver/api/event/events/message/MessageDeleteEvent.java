package cn.afternode.d4jserver.api.event.events.message;

import cn.afternode.d4jserver.api.event.eventapi.events.Event;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @see discord4j.core.event.domain.message.MessageDeleteEvent
 */
public final class MessageDeleteEvent implements Event {
    private final discord4j.core.event.domain.message.MessageDeleteEvent raw;

    public MessageDeleteEvent(discord4j.core.event.domain.message.MessageDeleteEvent raw, Guild guild) {
        if (raw.getMessage().isEmpty()) throw new NullPointerException("Message is null");

        this.raw = raw;
    }

    @Nullable
    public Message getMessage() {
        return raw.getMessage().get();
    }

    public Optional<Message> getMessageOptional() {
        return raw.getMessage();
    }

    public Guild getGuildAwait() {
        return raw.getGuild().block();
    }

    public Mono<Guild> getGuild() {
        return raw.getGuild();
    }

    public MessageChannel getChannelAwait() {
        return raw.getChannel().block();
    }

    public Mono<MessageChannel> getChannel() {
        return raw.getChannel();
    }
}
