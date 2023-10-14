package cn.afternode.d4jserver.commands;

import cn.afternode.d4jserver.api.D4JServerAPI;
import cn.afternode.d4jserver.api.command.Command;
import cn.afternode.d4jserver.api.command.CommandExecutor;
import cn.afternode.d4jserver.api.command.CommandSender;
import cn.afternode.d4jserver.api.plugin.D4JServerPlugin;
import cn.afternode.d4jserver.api.plugin.Plugin;
import cn.afternode.d4jserver.impl.D4JServerImpl;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateSpec;
import org.apache.commons.lang3.ArrayUtils;

@Command(name = "plugin", description = "Plugin operations", usage = "plugin <list/disable/enable/info> (name)")
public class PluginCommand implements CommandExecutor {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            usage(sender);
            return;
        }

        switch (args[0]) {
            case "list" -> list(sender);
            case "disable" -> {
                if (args.length != 2) usage(sender);
                disable(sender, args[1]);
            }
            case "enable" -> {
                if (args.length != 2) usage(sender);
                enable(sender, args[1]);
            }
            case "info" -> {
                if (args.length != 2) usage(sender);
                info(sender, args[1]);
            }
            default -> usage(sender);
        }
    }

    private void usage(CommandSender sender) {
        sender.reply(MessageCreateSpec.builder().content(D4JServerImpl.getInstance().getConfig().getCommandPrefix() + "plugin <list/disable/enable/info> (name)"));
    }

    private void list(CommandSender sender) {
        StringBuilder header = new StringBuilder();
        StringBuilder content = new StringBuilder();
        Plugin[] plugins = D4JServerAPI.getServer().getPluginManager().getPlugins();

        int enabled = 0;
        for (Plugin p: plugins) {
            if (p.isEnabled()) enabled++;
            if (content.toString().isEmpty()) content.append(p.getMeta().name()); else content.append(", ").append(p.getMeta().name());
        }

        header.append("Plugins (").append(enabled).append("/").append(plugins.length).append("): \n");

        sender.reply(MessageCreateSpec.builder().content(header.append(content).toString()));
    }

    private void disable(CommandSender sender, String name) {
        Plugin p = D4JServerAPI.getServer().getPluginManager().getPlugin(name);

        if (p == null) {
            sender.reply(MessageCreateSpec.builder().content("Invalid plugin " + name));
        } else {
            if (!p.isEnabled()) {
                sender.reply(MessageCreateSpec.builder().content("Plugin " + name + " already disabled"));
            } else {
                try {
                    p.disable();
                } catch (Throwable t) {
                    sender.reply(MessageCreateSpec.builder().content("Cannot disable this plugin, please check console"));
                    D4JServerImpl.getInstance().getLogger().warn("Cannot disable  plugin " + name + ", requested by user " + sender.getName() + " (" + sender.getId() + ")", t);
                }
                sender.reply(MessageCreateSpec.builder().content("Plugin " + name + " disabled successfully"));
            }
        }
    }

    private void enable(CommandSender sender, String name) {
        Plugin p = D4JServerAPI.getServer().getPluginManager().getPlugin(name);

        if (p == null) {
            sender.reply(MessageCreateSpec.builder().content("Invalid plugin " + name));
        } else {
            if (p.isEnabled()) {
                sender.reply(MessageCreateSpec.builder().content("Plugin " + name + " already enabled"));
            } else {
                try {
                    p.enable();
                } catch (Throwable t) {
                    sender.reply(MessageCreateSpec.builder().content("Cannot enable this plugin, please check console"));
                    D4JServerImpl.getInstance().getLogger().warn("Cannot enable  plugin " + name + ", requested by user " + sender.getName() + " (" + sender.getId() + ")", t);
                }
                sender.reply(MessageCreateSpec.builder().content("Plugin " + name + " enabled successfully"));
            }
        }
    }

    private void info(CommandSender sender, String name) {
        Plugin p = D4JServerAPI.getServer().getPluginManager().getPlugin(name);

        if (p == null) {
            sender.reply(MessageCreateSpec.builder().content("Invalid plugin " + name));
        } else {
            StringBuilder sb = new StringBuilder();
            D4JServerPlugin meta = p.getMeta();
            sb.append("Name: ").append(meta.name()).append("\n");
            sb.append("Version: ").append(meta.version()).append("\n");
            sb.append("Author: ").append(ArrayUtils.toString(meta.authors())).append("\n");
            sb.append("Description: ").append(meta.description());
            sender.reply(MessageCreateSpec.builder().content(sb.toString()));
        }
    }
}
