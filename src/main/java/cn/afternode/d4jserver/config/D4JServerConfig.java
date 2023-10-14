package cn.afternode.d4jserver.config;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class D4JServerConfig {
    @SerializedName("token")
    private String token = "";

    @SerializedName("command-prefix")
    private String commandPrefix = "/";

    @SerializedName("guild-limit")
    private GuildLimitConfig guildLimitConfig = new GuildLimitConfig();

    @SerializedName("messages")
    private MessagesConfig messagesConfig = new MessagesConfig();

    @SerializedName("maven-repositories")
    private List<MavenRepositoryConfig> maven = new ArrayList<>(Arrays.asList(
            new MavenRepositoryConfig("central", "https://repo1.maven.org/maven2/"),
            new MavenRepositoryConfig("jitpack", "https://jitpack.io")
    ));

    @SerializedName("database")
    private DatabaseConfig databaseConfig = new DatabaseConfig();
}
