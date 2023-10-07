package cn.afternode.d4jserver.config;

import lombok.Getter;

@Getter
public class MavenRepositoryConfig {
    private final String id;
    private final String url;
    private String type = "default";

    public MavenRepositoryConfig(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public MavenRepositoryConfig withType(String type) {
        this.type = type;
        return this;
    }
}
