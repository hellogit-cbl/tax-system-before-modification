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

public class TaxpayerInfoCsvDB2ExporterTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    private Connection connection;
    private Path tempCsv;

    @BeforeAll
    static void setupDb() {
        // テスト用PostgreSQLコンテナ起動
        postgres.start();
    }

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        // テストデータ投入（全カラム分ダミー値で埋める）
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO taxpayer_info VALUES (?,?,?,?,?,true,'151000','0','01','1','1000001','東京都千代田区霞が関1-1-1','131016','0001','東京都','千代田区','霞が関','1-1-1','霞が関マンション101','ｻﾄｳ ﾀﾛｳ','ｻﾄｳ','ﾀﾛｳ','テスト太郎','佐藤','太郎','1','1980-01-01','01','02','03','04',NULL,NULL,NULL,NULL,'T61','01','01','001','2028-05-31','1','JPN','日本','1','2000-04-01','2022-01-01','2022-01-02','01','2022-03-01','2022-02-10','2022-03-10','2022-04-01','2022-04-10','21','123456789012','1000014','131016','0001','東京都','千代田区','霞が関','1-1-1','霞が関アパート201','265','アメリカ合衆国','1234 APPLE STREET #101 SEATTLE WA','1010001','131016','0002','東京都','新宿区','西新宿','2-2-2','西新宿アパート202','日の丸 一郎','12345678913','01','東京都千代田区霞が関二丁目１番地','東京都','千代田区','霞が関二丁目','１番地',1000000,1000000,'1',2000000,2000000,'1','2022-01-01','2023-01-01','2022-06-01','2022-06-15','01',1000000,1000000,'1','1','01','2022-01-01','2022-02-01','2022-03-01','2022-04-01','1','131016','東京都','千代田区','霞が関二丁目','１番地','霞が関二丁目アパート301','1','1','131016','東京都','千代田区','霞が関二丁目','１番地','霞が関二丁目アパート302','1','01','1','1','1234567890','99999','2020-04-01','2021-03-01','メモサンプル',false,'admin','2024-07-24','09:00:00')")) {
            ps.setString(1, "123");
            ps.setString(2, "001");
            ps.setInt(3, 2024);
            ps.setString(4, "TAX001");
            ps.setInt(5, 1);
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
