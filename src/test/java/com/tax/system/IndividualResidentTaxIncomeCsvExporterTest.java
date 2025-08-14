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
public class IndividualResidentTaxIncomeCsvExporterTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private static Connection connection;
    private static final String CSV_PATH = "./test_income.csv";

    @BeforeAll
    static void setupDb() throws Exception {
        postgres.start();
        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS individual_resident_tax_income (city_code VARCHAR(6), tax_year INTEGER, taxpayer_no VARCHAR(15), assessment_history_no INTEGER, income_code VARCHAR(3), is_latest BOOLEAN, city_ward_code VARCHAR(12), income_amount NUMERIC(13), is_deleted BOOLEAN, operator_id VARCHAR(10), operated_at DATE, operated_time TIME)");
            stmt.execute("INSERT INTO individual_resident_tax_income VALUES ('131016',2024,'A00001',1,'101',true,'151000',4800000,false,'admin','2024-07-24','11:00:00')");
        }
    }

    @AfterAll
    static void cleanup() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        if (postgres != null) {
            postgres.stop();
        }
        Files.deleteIfExists(new File(CSV_PATH).toPath());
    }

    @Test
    void testExportToCsv() throws Exception {
        // パラメータファイルからcity_codeを取得
        java.util.Properties params = new java.util.Properties();
        try (java.io.FileReader reader = new java.io.FileReader("src/test/java/com/tax/system/params_income.txt")) {
            params.load(reader);
        }
        String cityCode = params.getProperty("city_code");
        String csvPath = params.getProperty("csv_path");
        IndividualResidentTaxIncomeCsvExporter exporter = new IndividualResidentTaxIncomeCsvExporter(connection);
        exporter.exportToCsv(cityCode, csvPath);
        assertTrue(new File(csvPath).exists());
        String content = Files.readString(new File(csvPath).toPath());
        assertTrue(content.contains("city_code"));
        assertTrue(content.contains("A00001"));
    }
}
