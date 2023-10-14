package cn.afternode.d4jserver.impl;

import cn.afternode.d4jserver.api.command.CommandSender;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MemberCommandSender implements CommandSender {
    private final Member member;
    private final Guild guild;
    private final Snowflake channel;
    private final Message message;

    MemberCommandSender(Member member, Guild guild, Snowflake channel, Message message) {
        this.member = member;
        this.guild = guild;
        this.channel = channel;
        this.message = message;
    }

    @Override
    public void reply(MessageCreateSpec.@NotNull Builder spec) {
        getMessageChannel().createMessage(spec.messageReference(message.getId()).build()).block();
    }

    @Override
    public boolean isConsole() {
        return false;
    }

    @Override
    public boolean hasPermission() {
        return false;
    }

    @Override
    public @Nullable Message getRawMessage() {
        return message;
    }

    @Override
    public @Nullable Member getMember() {
        return member;
    }

    @Override
    public @Nullable GuildChannel getGuildChannel() {
        return guild.getChannelById(channel).block();
    }

    @Override
    public MessageChannel getMessageChannel() {
        return message.getChannel().block();
    }

    @Override
    public @Nullable Guild getGuild() {
        return guild;
    }

    @Override
    public long getId() {
        return member.getId().asLong();
    }

    @Override
    public String getName() {
        return member.getGlobalName().orElse(member.getDisplayName());
    }
}
