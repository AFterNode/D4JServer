package cn.afternode.d4jserver.api.command;

import java.util.List;

public interface CommandManager {
    void register(CommandExecutor handler);
    CommandMap get(String name);
    List<CommandMap> getAllCommands();
}
