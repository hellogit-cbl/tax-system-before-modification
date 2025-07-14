package com.tax.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * PostgreSQL データベース接続クラス
 */
public class DatabaseConnection {
    private static final String DB_PROPERTIES_FILE = "/database.properties";
    private static Properties dbProperties;

    static {
        loadDatabaseProperties();
    }

    private static void loadDatabaseProperties() {
        dbProperties = new Properties();
        try (InputStream input = DatabaseConnection.class.getResourceAsStream(DB_PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + DB_PROPERTIES_FILE);
            }
            dbProperties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading database properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = dbProperties.getProperty("db.url");
        String username = dbProperties.getProperty("db.username");
        String password = dbProperties.getProperty("db.password");

        return DriverManager.getConnection(url, username, password);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}