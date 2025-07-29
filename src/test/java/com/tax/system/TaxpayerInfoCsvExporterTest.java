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
public class TaxpayerInfoCsvExporterTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private static Connection connection;
    private static final String CSV_PATH = "./test_taxpayer_info.csv";

    @BeforeAll
    static void setupDb() throws Exception {
        postgres.start();
        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS taxpayer_info (
                    city_code VARCHAR(6) NOT NULL,
                    pre_merge_city_code VARCHAR(6),
                    tax_year INTEGER,
                    taxpayer_no VARCHAR(15) NOT NULL,
                    person_hist_no INTEGER NOT NULL,
                    is_latest BOOLEAN,
                    city_ward_code VARCHAR(12),
                    undeclared_flag VARCHAR(1),
                    priority_tax_data_type VARCHAR(2),
                    resident_category VARCHAR(1),
                    addr_postal_code VARCHAR(7),
                    addr_full VARCHAR(300),
                    addr_city_code VARCHAR(6),
                    addr_town_code VARCHAR(7),
                    addr_prefecture VARCHAR(4),
                    addr_city_name VARCHAR(12),
                    addr_town VARCHAR(120),
                    addr_house_no VARCHAR(50),
                    addr_apartment VARCHAR(300),
                    name_kana VARCHAR(100),
                    last_name_kana VARCHAR(50),
                    first_name_kana VARCHAR(50),
                    name VARCHAR(100),
                    last_name VARCHAR(50),
                    first_name VARCHAR(50),
                    gender VARCHAR(1),
                    birth_date DATE,
                    relation_code1 VARCHAR(2),
                    relation_code2 VARCHAR(2),
                    relation_code3 VARCHAR(2),
                    relation_code4 VARCHAR(2),
                    name_foreign_kanji VARCHAR(100),
                    name_foreign_roman VARCHAR(100),
                    alias_name VARCHAR(100),
                    alias_kana VARCHAR(100),
                    visa_status VARCHAR(3),
                    visa_period_year VARCHAR(2),
                    visa_period_month VARCHAR(2),
                    visa_period_day VARCHAR(3),
                    visa_expiry_date DATE,
                    name_priority_type VARCHAR(2),
                    nationality_code VARCHAR(3),
                    nationality_name VARCHAR(100),
                    resident_status VARCHAR(1),
                    resident_since DATE,
                    move_date DATE,
                    move_notice_date DATE,
                    move_reason VARCHAR(2),
                    move_out_planned_date DATE,
                    move_out_notice_date DATE,
                    move_out_confirmed_date DATE,
                    removal_notice_date DATE,
                    removal_move_date DATE,
                    removal_reason VARCHAR(2),
                    my_number VARCHAR(12),
                    prev_addr_postal_code VARCHAR(7),
                    prev_addr_city_code VARCHAR(6),
                    prev_addr_town_code VARCHAR(7),
                    prev_addr_prefecture VARCHAR(4),
                    prev_addr_city_name VARCHAR(12),
                    prev_addr_town VARCHAR(120),
                    prev_addr_house_no VARCHAR(50),
                    prev_addr_apartment VARCHAR(300),
                    prev_addr_country_code VARCHAR(3),
                    prev_addr_country_name VARCHAR(200),
                    prev_addr_overseas VARCHAR(300),
                    next_addr_postal_code VARCHAR(7),
                    next_addr_city_code VARCHAR(6),
                    next_addr_town_code VARCHAR(7),
                    next_addr_prefecture VARCHAR(4),
                    next_addr_city_name VARCHAR(12),
                    next_addr_town VARCHAR(120),
                    next_addr_house_no VARCHAR(50),
                    next_addr_apartment VARCHAR(300),
                    household_head VARCHAR(300),
                    household_no VARCHAR(15),
                    widow_divorced_flag VARCHAR(2),
                    domicile VARCHAR(100),
                    domicile_prefecture VARCHAR(4),
                    domicile_city_name VARCHAR(12),
                    domicile_town VARCHAR(120),
                    domicile_lot_no VARCHAR(50),
                    nhip_payment_special NUMERIC(13),
                    nhip_payment_regular NUMERIC(13),
                    nhip_status VARCHAR(1),
                    care_payment_special NUMERIC(16),
                    care_payment_regular NUMERIC(16),
                    care_status VARCHAR(1),
                    welfare_start_date DATE,
                    welfare_end_date DATE,
                    welfare_suspend_date DATE,
                    welfare_resume_date DATE,
                    welfare_type VARCHAR(2),
                    late_elderly_payment_special NUMERIC(13),
                    late_elderly_payment_regular NUMERIC(13),
                    late_elderly_status VARCHAR(1),
                    disability_cert_type VARCHAR(1),
                    disability_grade VARCHAR(2),
                    disability_cert_issue_date DATE,
                    disability_cert_return_date DATE,
                    disability_cert_reissue_date DATE,
                    mental_disability_expire_date DATE,
                    out_of_resident_tax_flag VARCHAR(1),
                    out_of_resident_tax_city_code VARCHAR(6),
                    out_of_resident_tax_prefecture VARCHAR(6),
                    out_of_resident_tax_city_name VARCHAR(12),
                    out_of_resident_tax_town VARCHAR(120),
                    out_of_resident_tax_house_no VARCHAR(50),
                    out_of_resident_tax_apartment VARCHAR(300),
                    out_of_resident_notice_result VARCHAR(1),
                    other_municipality_tax_flag VARCHAR(1),
                    other_municipality_code VARCHAR(6),
                    other_municipality_prefecture VARCHAR(6),
                    other_municipality_city_name VARCHAR(12),
                    other_municipality_town VARCHAR(120),
                    other_municipality_house_no VARCHAR(50),
                    other_municipality_apartment VARCHAR(300),
                    tax_declaration_send_flag VARCHAR(1),
                    declaration_transfer_term_type VARCHAR(2),
                    declaration_notice_send_flag VARCHAR(1),
                    office_house_tax_declaration_send_flag VARCHAR(1),
                    basic_pension_no VARCHAR(10),
                    city_tax_office_code VARCHAR(5),
                    overseas_period_start DATE,
                    overseas_period_end DATE,
                    memo VARCHAR(2000),
                    is_deleted BOOLEAN NOT NULL,
                    operator_id VARCHAR(10),
                    operated_at DATE,
                    operated_time TIME,
                    PRIMARY KEY (city_code, taxpayer_no, person_hist_no)
                )
            """);
            stmt.execute("INSERT INTO taxpayer_info " +
                    "(city_code, pre_merge_city_code, tax_year, taxpayer_no, person_hist_no, is_latest, is_deleted) " +
                    "VALUES ('131016', '001', 2024, 'A00001', 1, true, false)");
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
        TaxpayerInfoCsvExporter exporter = new TaxpayerInfoCsvExporter(connection);
        exporter.exportToCsv("131016", CSV_PATH);
        assertTrue(new File(CSV_PATH).exists());
        String content = Files.readString(new File(CSV_PATH).toPath());
        assertTrue(content.contains("city_code"));
        assertTrue(content.contains("A00001"));
        assertTrue(content.contains("pre_merge_city_code"));
        assertTrue(content.contains("2024"));
        // 必要に応じて他のカラム・値も検証できます
    }
}
