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
public class IndividualResidentTaxLedgerCsvExporterTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private static Connection connection;
    private static final String CSV_PATH = "./test_ledger.csv";

    @BeforeAll
    static void setupDb() throws Exception {
        postgres.start();
        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS individual_resident_tax_ledger (city_code VARCHAR(6), pre_merge_city_code VARCHAR(6), levy_year INTEGER, tax_year INTEGER, taxpayer_no VARCHAR(15), assessment_history_no INTEGER, is_latest BOOLEAN, city_ward_code VARCHAR(12), is_deleted BOOLEAN, operator_id VARCHAR(10), operated_at DATE, operated_time TIME)");
            stmt.execute("INSERT INTO individual_resident_tax_ledger VALUES ('131016',NULL,2024,2024,'A00001',1,true,'151000',false,'admin','2024-07-24','09:00:00')");
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
        IndividualResidentTaxLedgerCsvExporter exporter = new IndividualResidentTaxLedgerCsvExporter(connection);
        exporter.exportToCsv("131016", CSV_PATH);
        assertTrue(new File(CSV_PATH).exists());
        String content = Files.readString(new File(CSV_PATH).toPath());
        assertTrue(content.contains("city_code"));
        assertTrue(content.contains("A00001"));
    }
}
