package cn.afternode.d4jserver.api.command;

import java.lang.reflect.Method;

public record CommandMap(String name, Command annotation, CommandExecutor executor, boolean isAlias) {
}
