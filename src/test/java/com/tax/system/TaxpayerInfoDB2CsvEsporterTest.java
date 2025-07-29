package com.tax.system;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.*;
import java.nio.file.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class TaxpayerInfoCsvDB2ExporterTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    private Connection connection;
    private Path tempCsv;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        // テーブル作成（必要なカラムだけ抜粋。実際は全カラム追加）
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE taxpayer_info (
                    city_code VARCHAR(10),
                    pre_merge_city_code VARCHAR(10),
                    tax_year INTEGER,
                    taxpayer_no VARCHAR(20),
                    name VARCHAR(100)
                    -- ★必要に応じて他のカラムも追加
                )
            """);
        }

        // テストデータ投入
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO taxpayer_info (city_code, pre_merge_city_code, tax_year, taxpayer_no, name) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, "123");
            ps.setString(2, "001");
            ps.setInt(3, 2024);
            ps.setString(4, "TAX001");
            ps.setString(5, "テスト太郎");
            ps.executeUpdate();
        }

        // 一時CSVファイル生成
        tempCsv = Files.createTempFile("taxpayer_info_test", ".csv");
    }

    @AfterEach
    void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        if (tempCsv != null) {
            Files.deleteIfExists(tempCsv);
        }
    }

    @Test
    void testExportToCsv() throws Exception {
        // エクスポータ実行
        TaxpayerInfoCsvExporter exporter = new TaxpayerInfoCsvExporter(connection);
        exporter.exportToCsv("123", tempCsv.toString());

        // CSV内容を検証
        String csvContent = Files.readString(tempCsv);
        String[] lines = csvContent.split("\n");

        // 1行目: ヘッダー
        assertTrue(lines[0].contains("city_code"));
        assertTrue(lines[0].contains("pre_merge_city_code"));
        assertTrue(lines[0].contains("tax_year"));
        assertTrue(lines[0].contains("taxpayer_no"));
        assertTrue(lines[0].contains("name"));

        // 2行目: データ
        assertTrue(lines[1].contains("123"));
        assertTrue(lines[1].contains("001"));
        assertTrue(lines[1].contains("2024"));
        assertTrue(lines[1].contains("TAX001"));
        assertTrue(lines[1].contains("テスト太郎"));
    }
}
