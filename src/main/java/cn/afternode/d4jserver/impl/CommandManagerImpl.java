package cn.afternode.d4jserver.impl;

import cn.afternode.d4jserver.api.command.Command;
import cn.afternode.d4jserver.api.command.CommandExecutor;
import cn.afternode.d4jserver.api.command.CommandManager;
import cn.afternode.d4jserver.api.command.CommandMap;
import cn.afternode.d4jserver.api.event.eventapi.EventTarget;
import cn.afternode.d4jserver.api.event.eventapi.types.Priority;
import cn.afternode.d4jserver.api.event.events.message.MessageReceiveEvent;
import cn.afternode.d4jserver.commands.HelpCommand;
import cn.afternode.d4jserver.commands.PluginCommand;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManagerImpl implements CommandManager {
    private final HashMap<String, CommandMap> handlers = new HashMap<>();
    private final Logger logger = LogManager.getLogger("CommandManager");

    public void init() {
        register(new HelpCommand());
        register(new PluginCommand());
    }

    public void register(CommandExecutor handler) {
        if (!handler.getClass().isAnnotationPresent(Command.class)) throw new IllegalArgumentException(handler.getClass().getName() + " has no Command annotation present");
        Command anno = handler.getClass().getAnnotation(Command.class);
        if (handlers.containsKey(anno.name())) throw new IllegalArgumentException("Duplicate command name " + anno.name());

        handlers.put(anno.name(), new CommandMap(anno.name(), anno, handler, false));
        for (String s: anno.alias()) {
            if (handlers.containsKey(s)) throw new IllegalArgumentException("Duplicate alias name " + s);
            handlers.put(s, new CommandMap(s, anno, handler, true));
        }
    }

    @Override
    public CommandMap get(String name) {
        return handlers.get(name);
    }

    @Override
    public List<CommandMap> getAllCommands() {
        return new ArrayList<>(handlers.values());
    }

    @EventTarget(Priority.HIGHEST)
    public void receiveMessage(MessageReceiveEvent event) {
        String content = event.getMessage().getContent();
        if (content.startsWith(D4JServerImpl.getInstance().getConfig().getCommandPrefix())) {
            content = content.replaceFirst(D4JServerImpl.getInstance().getConfig().getCommandPrefix(), "");
            String[] parts = content.split(" ");
            Member member = event.getMember();
            MessageChannel channel = event.getMessage().getChannel().block();
            if (channel == null) return;

            if (handlers.containsKey(parts[0])) {
                String[] args = content.replaceFirst(parts[0] + " ", "").split(" ");

                CommandMap cm = handlers.get(parts[0]);
                try {
                    cm.executor().onCommand(new MemberCommandSender(event.getMember(), event.getGuild(),
                            event.getMessage().getChannelId(), event.getMessage()), args);
                } catch (Throwable t) {
                    logger.warn("Cannot pass command " + parts[0] + " from " + member.getGlobalName().orElse(member.getDisplayName()) + " to executor " + cm.executor().getClass().getName());
                }
            } else {
                channel.createMessage(
                        MessageCreateSpec.
                                builder().
                                messageReference(event.getMessage().getId()).content(D4JServerImpl.getInstance().getConfig().getMessagesConfig().getCmdInvalidCommand()).
                                build()
                ).block();
            }
        }
    }
}
