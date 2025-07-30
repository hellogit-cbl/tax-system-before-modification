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
public class IndividualResidentTaxAssessmentCsvExporterTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private static Connection connection;
    private static final String CSV_PATH = "./test_assessment.csv";

    @BeforeAll
    static void setupDb() throws Exception {
        postgres.start();
        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS individual_resident_tax_assessment (city_code VARCHAR(6), tax_year INTEGER, taxpayer_no VARCHAR(15), assessment_history_no INTEGER, city_ward_code VARCHAR(12), is_latest BOOLEAN, collection_type VARCHAR(1), tax_exemption_type VARCHAR(1), forest_env_tax_exemption_type VARCHAR(1), pension_type VARCHAR(3), non_taxable_reason_type VARCHAR(2), blue_white_type VARCHAR(1), spouse_special_type VARCHAR(1), self_special_type VARCHAR(1), change_reason VARCHAR(3), change_date DATE, tax_notice_send_date DATE, annual_tax_amount INTEGER, is_deleted BOOLEAN, operated_at DATE)");
            stmt.execute("INSERT INTO individual_resident_tax_assessment VALUES ('131016',2024,'A00001',1,'151000',true,'1','1','1','T61','01','1','1','1','01','2024-02-01','2024-05-10',200000,false,'2024-07-24')");
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
        IndividualResidentTaxAssessmentCsvExporter exporter = new IndividualResidentTaxAssessmentCsvExporter(connection);
        exporter.exportToCsv("131016", CSV_PATH);
        assertTrue(new File(CSV_PATH).exists());
        String content = Files.readString(new File(CSV_PATH).toPath());
        assertTrue(content.contains("city_code"));
        assertTrue(content.contains("A00001"));
    }
}
