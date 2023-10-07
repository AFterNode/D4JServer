package cn.afternode.d4jserver.commands;

import cn.afternode.d4jserver.api.D4JServerAPI;
import cn.afternode.d4jserver.api.command.Command;
import cn.afternode.d4jserver.api.command.CommandExecutor;
import cn.afternode.d4jserver.api.command.CommandMap;
import cn.afternode.d4jserver.api.command.CommandSender;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

@Command(name = "help", alias = {}, usage = "/help (command)")
public class HelpCommand implements CommandExecutor {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            StringBuilder commands = new StringBuilder();

            List<CommandMap> cmd = D4JServerAPI.getServer().getCommandManager().getAllCommands();
            commands.append("Available Commands (").append(cmd.size()).append("):");
            for (CommandMap cm: cmd) {
                if (!cm.isAlias()) commands.append("\n").append(cm.name());
            }

            sender.reply(
                    MessageCreateSpec.builder()
                            .content(commands.toString())
            );
        } else {
            String commandName = args[0];
            CommandMap map = D4JServerAPI.getServer().getCommandManager().get(commandName);

            if (map != null) {
                sender.reply(
                        MessageCreateSpec.builder()
                                .content("""
                                        Command Name: %name%
                                        Aliases: %alias%
                                        Usage: %usage%
                                        Description: %desc%
                                        """.trim()
                                        .replace("%name%", map.name())
                                        .replace("%alias%", ArrayUtils.toString(map.annotation().alias()))
                                        .replace("%usage%", map.annotation().usage())
                                        .replace("%desc%", map.annotation().description()))
                );
            } else {
                sender.reply(
                        MessageCreateSpec.builder()
                                .content("Cannot find command with name " + commandName)
                );
            }
        }
    }
}
