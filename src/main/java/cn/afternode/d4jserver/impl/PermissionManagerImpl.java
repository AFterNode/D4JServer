package cn.afternode.d4jserver.impl;

import cn.afternode.d4jserver.api.permission.Permission;
import cn.afternode.d4jserver.api.permission.PermissionManager;
import discord4j.core.object.entity.User;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PermissionManagerImpl implements PermissionManager {
    private static final Permission root = new Permission("*");
    private final Logger logger = LogManager.getLogger("Permissions");
    private final HashMap<String, Permission> registry = new HashMap<>();

    @Override
    public Permission getPermission(String name) {
        if (registry.containsKey(name)) return registry.get(name);

        Permission perm = new Permission(name);
        register(perm);
        return perm;
    }

    public void register(Permission perm) {
        this.registry.put(perm.getName(), perm);
    }

    @Override
    public Permission[] getUserPermissions(User user) {
        Connection conn = D4JServerImpl.getInstance().getDatabaseManager().getConnection();

        List<Permission> perm = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT perm FROM UserPerm WHERE id=?")) {
            stmt.setLong(1, user.getId().asLong());
            String result = stmt.executeQuery().getString("perm");
            if (result == null) {
                try (PreparedStatement s0 = conn.prepareStatement("INSERT INTO UserPerm VALUES(id=?, perm=?)")) {
                    s0.setLong(1, user.getId().asLong());
                    s0.setString(2, "");
                    s0.execute();
                } catch (Throwable t) {
                    throw new RuntimeException("Cannot update user permission", t);
                }
            } else {
                for (String part: result.split(";")) {
                    if (part.isBlank() || part.isEmpty()) continue;
                    perm.add(getPermission(part));
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Cannot query user permission", t);
        }

        return perm.toArray(new Permission[0]);
    }

    @Override
    public boolean hasPermission(User user, Permission permission) {
        for (Permission p: getUserPermissions(user)) {
            if (p.equals(permission)) return true;
        }

        return false;
    }

    @Override
    public void addUserPermission(User user, Permission permission) {
        List<Permission> original = new ArrayList<>();
        Collections.addAll(original, getUserPermissions(user));
        StringBuilder sb = new StringBuilder();
        for (Permission p: original) {
            sb.append(p.getName()).append(";");
        }
        sb.append(permission.getName());

        Connection conn = D4JServerImpl.getInstance().getDatabaseManager().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement("UPDATE UserPerm SET perm=? WHERE id=?")) {
            stmt.setString(1, sb.toString());
            stmt.setLong(2, user.getId().asLong());
        } catch (Throwable t) {
            throw new RuntimeException("Cannot add user permission", t);
        }
    }
}
