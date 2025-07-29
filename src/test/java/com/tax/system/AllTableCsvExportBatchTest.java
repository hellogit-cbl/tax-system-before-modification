package com.tax.system;

import org.junit.jupiter.api.*;
import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class AllTableCsvExportBatchTest {
    private static Connection connection;
    private static final String OUT_DIR = "./test_output";

    @BeforeAll
    static void setupDb() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb3;DB_CLOSE_DELAY=-1");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE taxpayer_info (city_code VARCHAR(6), taxpayer_no VARCHAR(15), person_hist_no INTEGER, is_latest BOOLEAN, is_deleted BOOLEAN)");
            stmt.execute("INSERT INTO taxpayer_info VALUES ('131016','A00001',1,true,false)");
            stmt.execute("CREATE TABLE individual_resident_tax_assessment (city_code VARCHAR(6), tax_year INTEGER, taxpayer_no VARCHAR(15), assessment_history_no INTEGER, is_deleted BOOLEAN)");
            stmt.execute("INSERT INTO individual_resident_tax_assessment VALUES ('131016',2024,'A00001',1,false)");
        }
        new File(OUT_DIR).mkdirs();
    }

    @AfterAll
    static void cleanup() throws Exception {
        connection.close();
        for (File f : new File(OUT_DIR).listFiles()) {
            f.delete();
        }
        new File(OUT_DIR).delete();
    }

    @Test
    void testExportAll() throws Exception {
        AllTableCsvExportBatch batch = new AllTableCsvExportBatch(connection);
        batch.exportAll("131016", OUT_DIR);
        assertTrue(new File(OUT_DIR + "/taxpayer_info.csv").exists());
        assertTrue(new File(OUT_DIR + "/individual_resident_tax_assessment.csv").exists());
    }
}
