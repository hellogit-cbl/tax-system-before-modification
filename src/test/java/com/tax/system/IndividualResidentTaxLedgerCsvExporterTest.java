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
            stmt.execute("CREATE TABLE IF NOT EXISTS individual_resident_tax_ledger (city_code VARCHAR(6) NOT NULL, pre_merge_city_code VARCHAR(6), levy_year INTEGER NOT NULL, tax_year INTEGER NOT NULL, taxpayer_no VARCHAR(15) NOT NULL, assessment_history_no INTEGER NOT NULL, is_latest BOOLEAN, city_ward_code VARCHAR(12), normal_due_date1 DATE, normal_due_date2 DATE, normal_due_date3 DATE, normal_due_date4 DATE, normal_due_date5 DATE, normal_due_date6 DATE, normal_due_date7 DATE, normal_due_date8 DATE, normal_due_date9 DATE, normal_due_date10 DATE, normal_due_date11 DATE, normal_due_date12 DATE, notice_date DATE, normal_amount1 INTEGER, normal_amount2 INTEGER, normal_amount3 INTEGER, normal_amount4 INTEGER, normal_amount5 INTEGER, normal_amount6 INTEGER, normal_amount7 INTEGER, normal_amount8 INTEGER, normal_amount9 INTEGER, normal_amount10 INTEGER, normal_amount11 INTEGER, normal_amount12 INTEGER, normal_paid_amount1 INTEGER, normal_paid_amount2 INTEGER, normal_paid_amount3 INTEGER, normal_paid_amount4 INTEGER, normal_paid_amount5 INTEGER, normal_paid_amount6 INTEGER, normal_paid_amount7 INTEGER, normal_paid_amount8 INTEGER, normal_paid_amount9 INTEGER, normal_paid_amount10 INTEGER, normal_paid_amount11 INTEGER, normal_paid_amount12 INTEGER, normal_sp_annual_amount1 INTEGER, normal_sp_annual_amount2 INTEGER, normal_sp_annual_amount3 INTEGER, normal_sp_annual_amount4 INTEGER, normal_sp_annual_amount5 INTEGER, normal_sp_annual_amount6 INTEGER, normal_sp_annual_amount7 INTEGER, normal_sp_annual_amount8 INTEGER, normal_sp_annual_amount9 INTEGER, normal_sp_annual_amount10 INTEGER, normal_sp_annual_amount11 INTEGER, normal_sp_annual_amount12 INTEGER, notice_no VARCHAR(20), special_agent_no1 VARCHAR(12), special_agent_no2 VARCHAR(12), special_agent_no3 VARCHAR(12), special_agent_no4 VARCHAR(12), special_agent_no5 VARCHAR(12), special_agent_no6 VARCHAR(12), special_agent_no7 VARCHAR(12), special_agent_no8 VARCHAR(12), special_agent_no9 VARCHAR(12), special_agent_no10 VARCHAR(12), special_agent_no11 VARCHAR(12), special_agent_no12 VARCHAR(12), special_amount1 INTEGER, special_amount2 INTEGER, special_amount3 INTEGER, special_amount4 INTEGER, special_amount5 INTEGER, special_amount6 INTEGER, special_amount7 INTEGER, special_amount8 INTEGER, special_amount9 INTEGER, special_amount10 INTEGER, special_amount11 INTEGER, special_amount12 INTEGER, special_paid_amount1 INTEGER, special_paid_amount2 INTEGER, special_paid_amount3 INTEGER, special_paid_amount4 INTEGER, special_paid_amount5 INTEGER, special_paid_amount6 INTEGER, special_paid_amount7 INTEGER, special_paid_amount8 INTEGER, special_paid_amount9 INTEGER, special_paid_amount10 INTEGER, special_paid_amount11 INTEGER, special_paid_amount12 INTEGER, nencho_due_date_apr DATE, nencho_due_date_jun DATE, nencho_due_date_aug DATE, nencho_due_date_oct DATE, nencho_due_date_dec DATE, nencho_due_date_feb DATE, nencho_amount_apr INTEGER, nencho_amount_jun INTEGER, nencho_amount_aug INTEGER, nencho_amount_oct INTEGER, nencho_amount_dec INTEGER, nencho_amount_feb INTEGER, next_nencho_amount_apr INTEGER, next_nencho_amount_jun INTEGER, next_nencho_amount_aug INTEGER, is_deleted BOOLEAN NOT NULL, operator_id VARCHAR(10), operated_at DATE, operated_time TIME, PRIMARY KEY (city_code, levy_year, tax_year, taxpayer_no, assessment_history_no))");
            stmt.execute("INSERT INTO individual_resident_tax_ledger VALUES ('131016',NULL,2024,2024,'A00001',1,true,'151000','2024-04-01','2024-05-01','2024-06-01','2024-07-01','2024-08-01','2024-09-01','2024-10-01','2024-11-01','2024-12-01','2025-01-01','2025-02-01','2025-03-01','2024-04-10',10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,9000,9000,9000,9000,9000,9000,9000,9000,9000,9000,9000,9000,8000,8000,8000,8000,8000,8000,8000,8000,8000,8000,8000,8000,'N001','A001','A002','A003','A004','A005','A006','A007','A008','A009','A010','A011','A012',5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,4000,4000,4000,4000,4000,4000,4000,4000,4000,4000,4000,4000,'2024-06-01','2024-08-01','2024-10-01','2024-12-01','2025-02-01','2025-04-01',6000,6000,6000,6000,6000,6000,7000,7000,7000,false,'admin','2024-07-24','09:00:00')");
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
