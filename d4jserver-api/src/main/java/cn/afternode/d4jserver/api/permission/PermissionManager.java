package cn.afternode.d4jserver.api.permission;

import discord4j.core.object.entity.User;

public interface PermissionManager {
    Permission getPermission(String name);

    Permission[] getUserPermissions(User user);

    boolean hasPermission(User user, Permission permission);

    void addUserPermission(User user, Permission permission);
}
