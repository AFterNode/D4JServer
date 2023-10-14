package cn.afternode.d4jserver.config;

import lombok.Getter;

@Getter
public class DatabaseConfig {
    private String type = "h2";

    private String host = "127.0.0.1";

    private int port = 3306;

    private String username = "D4JServer";

    private String password = "D4JServer";

    private String database = "D4JServer";
}
