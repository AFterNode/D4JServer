package cn.afternode.d4jserver.api.command;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandSender {
    void reply(@NotNull MessageCreateSpec.Builder spec);

    boolean isConsole();

    boolean hasPermission();

    /**
     * Null if this sender is the console
     */
    @Nullable Message getRawMessage();

    /**
     * Null if this sender is the console
     */
    @Nullable Member getMember();

    /**
     * Null if this sender is the console
     */
    @Nullable
    GuildChannel getGuildChannel();

    /**
     * Null if this sender is the console
     */
    MessageChannel getMessageChannel();

    /**
     * Null if this sender is the console
     */
    @Nullable Guild getGuild();

    String getName();

}