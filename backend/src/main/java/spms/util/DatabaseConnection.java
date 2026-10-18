package spms.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Shared JDBC connection provider. Process environment values override local .env settings. */
public final class DatabaseConnection {
    private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    private final Properties local = readEnvFile();
    private DatabaseConnection() {}
    public static DatabaseConnection getInstance() { return INSTANCE; }

    public Connection open() throws SQLException {
        String username = value("DB_USERNAME", "");
        if (username.isBlank()) throw new SQLException("Database login is not configured. Set DB_USERNAME in .env.");
        String url = "jdbc:mysql://" + value("DB_HOST", "localhost") + ":" + value("DB_PORT", "3306") + "/" + value("DB_NAME", "SPMS_DB")
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        return DriverManager.getConnection(url, username, value("DB_PASSWORD", ""));
    }
    private String value(String key, String fallback) {
        String env = System.getenv(key);
        String value = env == null || env.isBlank() ? local.getProperty(key) : env;
        return value == null || value.isBlank() ? fallback : value.trim();
    }
    private static Properties readEnvFile() {
        Properties result = new Properties(); Path file = Path.of(".env");
        if (!Files.isRegularFile(file)) return result;
        try { for (String line : Files.readAllLines(file)) { String s=line.trim(); if(s.isEmpty()||s.startsWith("#"))continue; int i=s.indexOf('='); if(i>0)result.setProperty(s.substring(0,i).trim(),s.substring(i+1).trim().replaceAll("^['\"]|['\"]$", "")); } }
        catch (IOException e) { throw new IllegalStateException("Could not read local database configuration.", e); }
        return result;
    }
}
