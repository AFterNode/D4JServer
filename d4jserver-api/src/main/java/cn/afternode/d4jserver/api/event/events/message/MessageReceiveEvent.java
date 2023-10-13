package cn.afternode.d4jserver.api.event.events.message;

import cn.afternode.d4jserver.api.event.eventapi.events.callables.EventCancellable;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.Opt;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Called by MessageCreateEvent
 * Cancel to delete this message
 * @see discord4j.core.event.domain.message.MessageCreateEvent
 */
@Getter
public class MessageReceiveEvent extends EventCancellable {
    private final MessageCreateEvent raw;

    private final Message message;
    private final Guild guild;
    private final Member member;

    @Setter
    private String deleteReason = null;

    public MessageReceiveEvent(MessageCreateEvent event, Guild guild) {
        if (event.getMember().isEmpty()) throw new NullPointerException("MessageReceiveEvent member not present");

        this.raw = event;

        this.message = event.getMessage();
        this.guild = guild;
        this.member = event.getMember().get();
    }

    public Optional<Member> getMemberOptional() {
        return raw.getMember();
    }

    @Nullable public Member getMemberOr(Member def) {
        return getMemberOptional().orElse(def);
    }

    @Nullable
    public Member getMemberOrNull() {
        return getMemberOr(null);
    }
}
