package com.tax.system;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class DatabaseConnectionTest {

    @Container
    private static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("tax_db_test")
            .withUsername("tax_user")
            .withPassword("tax_password");

    private Connection connection;

    @org.junit.jupiter.api.BeforeAll
    static void startContainer() {
        postgresql.start();
    }

    @org.junit.jupiter.api.AfterAll
    static void stopContainer() {
        postgresql.stop();
    }

    @BeforeEach
    void setUp() throws SQLException {
        String jdbcUrl = postgresql.getJdbcUrl();
        String username = postgresql.getUsername();
        String password = postgresql.getPassword();

        connection = DriverManager.getConnection(jdbcUrl, username, password);

        // テーブル作成
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS tax_assessment (
                    id SERIAL PRIMARY KEY,
                    municipality_code VARCHAR(6) NOT NULL,
                    assessment_year INTEGER NOT NULL,
                    individual_number VARCHAR(12) NOT NULL,
                    history_number INTEGER NOT NULL DEFAULT 1,
                    taxpayer_name VARCHAR(100) NOT NULL,
                    tax_amount DECIMAL(15,2) NOT NULL,
                    tax_notice_issue_date DATE,
                    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
            """);
        }
    }

    // ★追加ここから
    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    // ★追加ここまで

    @Test
    void testDatabaseConnection() {
        assertNotNull(connection);
        assertTrue(postgresql.isRunning());
    }

    @Test
    void testDataExtraction() throws SQLException {
        // テストデータ挿入
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                INSERT INTO tax_assessment 
                (municipality_code, assessment_year, individual_number, 
                 taxpayer_name, tax_amount) 
                VALUES 
                ('001', 2024, '123456789012', 'テスト太郎', 100000.00),
                ('001', 2024, '123456789013', 'テスト次郎', 150000.00)
            """);
        }

        // データ確認
        try (Statement stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) FROM tax_assessment")) {
            if (rs.next()) {
                assertEquals(2, rs.getInt(1));
            }
        }
    }
}
