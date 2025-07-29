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
public class AllTableCsvExportBatchTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private static Connection connection;
    private static final String OUT_DIR = "./test_output";

    @BeforeAll
    static void setupDb() throws Exception {
        postgres.start();
        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS taxpayer_info (city_code VARCHAR(6), taxpayer_no VARCHAR(15), person_hist_no INTEGER, is_latest BOOLEAN, is_deleted BOOLEAN)");
            stmt.execute("INSERT INTO taxpayer_info VALUES ('131016','A00001',1,true,false)");
            stmt.execute("CREATE TABLE IF NOT EXISTS individual_resident_tax_assessment (city_code VARCHAR(6), tax_year INTEGER, taxpayer_no VARCHAR(15), assessment_history_no INTEGER, city_ward_code VARCHAR(12), is_latest BOOLEAN, collection_type VARCHAR(1), tax_exemption_type VARCHAR(1), forest_env_tax_exemption_type VARCHAR(1), pension_type VARCHAR(3), non_taxable_reason_type VARCHAR(2), blue_white_type VARCHAR(1), spouse_special_type VARCHAR(1), self_special_type VARCHAR(1), change_reason VARCHAR(3), change_date DATE, tax_notice_send_date DATE, annual_tax_amount INTEGER, is_deleted BOOLEAN, operated_at DATE)");
            stmt.execute("INSERT INTO individual_resident_tax_assessment VALUES ('131016',2024,'A00001',1,'151000',true,'1','1','1','T61','01','1','1','1','01','2024-02-01','2024-05-10',200000,false,'2024-07-24')");
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
    void testExportAll() throws Exception {
        AllTableCsvExportBatch batch = new AllTableCsvExportBatch(connection);
        batch.exportAll("131016", OUT_DIR);
        assertTrue(new File(OUT_DIR + "/taxpayer_info.csv").exists());
        assertTrue(new File(OUT_DIR + "/individual_resident_tax_assessment.csv").exists());
    }
}
