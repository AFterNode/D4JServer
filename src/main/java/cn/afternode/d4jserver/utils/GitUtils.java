package cn.afternode.d4jserver.utils;

import cn.afternode.d4jserver.Main;
import lombok.Getter;

import java.util.Properties;

public class GitUtils {
    @Getter
    private static final Properties properties = new Properties();

    public static String getBranch() {
        return properties.getProperty("git.branch", "unknown");
    }

    public static String getBuildUsername() {
        return properties.getProperty("git.build.user.name", "unknown");
    }

    public static String getVersion() {
        return properties.getProperty("git.build.version", "unknown");
    }

    public static String getAbbrev() {
        return properties.getProperty("git.commit.id.abbrev", "unknown");
    }

    public static String getCommitId() {
        return properties.getProperty("git.commit.id", "unknown");
    }

    static {
        try {
            properties.load(Main.class.getResourceAsStream("git.properties"));
        } catch (Throwable t) {
            throw new RuntimeException("GitUtils initialization failed", t);
        }
    }
}
