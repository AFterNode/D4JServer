package cn.afternode.d4jserver.db;

import cn.afternode.d4jserver.config.DatabaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DatabaseManager {
    private final DatabaseConfig config;
    private final Logger logger = LogManager.getLogger("Database");

    private Connection connection;

    public DatabaseManager(DatabaseConfig config) {
        this.config = config;
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if (config.getType().equalsIgnoreCase("h2")) {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:./d4j-server.db");
        } else if (config.getType().equalsIgnoreCase("mysql")) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase(), config.getUsername(), config.getPassword());
        }
    }

    public void init() throws SQLException {
        logger.info("Initializing database");

        Statement stmt = connection.createStatement();
        stmt.close();
    }

    public PreparedStatement prepare(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }
}
