package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:h2:./data/appdb;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private DatabaseManager() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initialize() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name  VARCHAR(100) NOT NULL,
                    email VARCHAR(200) NOT NULL UNIQUE,
                    age   INT NOT NULL
                )
            """);
        } catch (SQLException e) {
            throw new RuntimeException("DB 초기화 실패", e);
        }
    }
}
