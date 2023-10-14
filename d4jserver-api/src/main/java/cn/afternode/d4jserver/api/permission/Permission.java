package cn.afternode.d4jserver.api.permission;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Permission {
    private final String name;

    public Permission(String name) {
        this.name = name;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Permission) {
            return name.equalsIgnoreCase(((Permission) obj).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Permission{" +
                "name='" + name + '\'' +
                '}';
    }
}
