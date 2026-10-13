package com.shopsphere.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database utility class managing JDBC Connections to MySQL.
 * Demonstrates RTU syllabus: java.sql.DriverManager, Connection, SQLException handling.
 */
public class DBUtil {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/shopsphere?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found in classpath: " + e.getMessage());
        }
    }

    public static String getUrl() {
        return System.getenv().getOrDefault("DB_URL", DEFAULT_URL);
    }

    public static String getUser() {
        return System.getenv().getOrDefault("DB_USER", DEFAULT_USER);
    }

    public static String getPassword() {
        return System.getenv().getOrDefault("DB_PASS", DEFAULT_PASS);
    }

    /**
     * Obtains a new JDBC Connection. Caller is responsible for closing (use try-with-resources).
     */
    public static Connection get() throws SQLException {
        return DriverManager.getConnection(getUrl(), getUser(), getPassword());
    }

    /**
     * Closes resources quietly.
     */
    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }
}
