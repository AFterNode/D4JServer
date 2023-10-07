package cn.afternode.d4jserver.api.command;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public interface CommandExecutor {
    void onCommand(CommandSender sender, String[] args);
}
