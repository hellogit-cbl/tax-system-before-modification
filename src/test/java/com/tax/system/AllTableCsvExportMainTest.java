package com.tax.system;

import org.junit.jupiter.api.*;
import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class AllTableCsvExportMainTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private static Connection connection;
    private static final String OUT_DIR = "./test_output_main";

    @BeforeAll
    static void setupDb() throws Exception {
        postgres.start();
        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        try (Statement stmt = connection.createStatement()) {
            // 必要なテーブル定義（最小限: taxpayer_infoのみ例示。他も必要なら追加）
            stmt.execute("CREATE TABLE IF NOT EXISTS taxpayer_info (city_code VARCHAR(6) NOT NULL, pre_merge_city_code VARCHAR(6), tax_year INTEGER, taxpayer_no VARCHAR(15) NOT NULL, person_hist_no INTEGER NOT NULL, is_latest BOOLEAN, is_deleted BOOLEAN NOT NULL, PRIMARY KEY (city_code, taxpayer_no, person_hist_no))");
            stmt.execute("INSERT INTO taxpayer_info VALUES ('131016','001',2024,'A00001',1,true,false)");
        }
        new File(OUT_DIR).mkdirs();
    }

    @AfterAll
    static void cleanup() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        if (postgres != null) {
            postgres.stop();
        }
        File dir = new File(OUT_DIR);
        if (dir.exists()) {
            for (File f : dir.listFiles()) {
                f.delete();
            }
            dir.delete();
        }
    }

    @Test
    void testMainExport() throws Exception {
        // mainメソッド実行
        AllTableCsvExportMain.main(new String[]{});
        // 出力ファイルの存在確認（例: taxpayer_info.csv）
        File csv = new File("./output/taxpayer_info.csv");
        assertTrue(csv.exists(), "taxpayer_info.csvが出力されていること");
        // 内容検証（ヘッダー行が含まれること）
        String content = Files.readString(csv.toPath());
        assertTrue(content.contains("city_code"));
        assertTrue(content.contains("A00001"));
    }
}
