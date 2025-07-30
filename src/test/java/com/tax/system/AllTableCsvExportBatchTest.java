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
            stmt.execute("CREATE TABLE IF NOT EXISTS individual_resident_tax_deduction (city_code VARCHAR(6) NOT NULL, tax_year INTEGER NOT NULL, taxpayer_no VARCHAR(15) NOT NULL, assessment_history_no INTEGER NOT NULL, deduction_code VARCHAR(3) NOT NULL, is_latest BOOLEAN, city_ward_code VARCHAR(12), deduction_amount NUMERIC(13), is_deleted BOOLEAN NOT NULL, operator_id VARCHAR(10), operated_at DATE, operated_time TIME, PRIMARY KEY (city_code, tax_year, taxpayer_no, assessment_history_no, deduction_code))");
            stmt.execute("CREATE TABLE IF NOT EXISTS individual_resident_tax_income (city_code VARCHAR(6) NOT NULL, tax_year INTEGER NOT NULL, taxpayer_no VARCHAR(15) NOT NULL, assessment_history_no INTEGER NOT NULL, income_code VARCHAR(3) NOT NULL, is_latest BOOLEAN, city_ward_code VARCHAR(12), income_amount NUMERIC(13), is_deleted BOOLEAN NOT NULL, operator_id VARCHAR(10), operated_at DATE, operated_time TIME, PRIMARY KEY (city_code, tax_year, taxpayer_no, assessment_history_no, income_code))");
            stmt.execute("CREATE TABLE IF NOT EXISTS taxpayer_info (city_code VARCHAR(6) NOT NULL, pre_merge_city_code VARCHAR(6), tax_year INTEGER, taxpayer_no VARCHAR(15) NOT NULL, person_hist_no INTEGER NOT NULL, is_latest BOOLEAN, city_ward_code VARCHAR(12), undeclared_flag VARCHAR(1), priority_tax_data_type VARCHAR(2), resident_category VARCHAR(1), addr_postal_code VARCHAR(7), addr_full VARCHAR(300), addr_city_code VARCHAR(6), addr_town_code VARCHAR(7), addr_prefecture VARCHAR(4), addr_city_name VARCHAR(12), addr_town VARCHAR(120), addr_house_no VARCHAR(50), addr_apartment VARCHAR(300), name_kana VARCHAR(100), last_name_kana VARCHAR(50), first_name_kana VARCHAR(50), name VARCHAR(100), last_name VARCHAR(50), first_name VARCHAR(50), gender VARCHAR(1), birth_date DATE, relation_code1 VARCHAR(2), relation_code2 VARCHAR(2), relation_code3 VARCHAR(2), relation_code4 VARCHAR(2), name_foreign_kanji VARCHAR(100), name_foreign_roman VARCHAR(100), alias_name VARCHAR(100), alias_kana VARCHAR(100), visa_status VARCHAR(3), visa_period_year VARCHAR(2), visa_period_month VARCHAR(2), visa_period_day VARCHAR(3), visa_expiry_date DATE, name_priority_type VARCHAR(2), nationality_code VARCHAR(3), nationality_name VARCHAR(100), resident_status VARCHAR(1), resident_since DATE, move_date DATE, move_notice_date DATE, move_reason VARCHAR(2), move_out_planned_date DATE, move_out_notice_date DATE, move_out_confirmed_date DATE, removal_notice_date DATE, removal_move_date DATE, removal_reason VARCHAR(2), my_number VARCHAR(12), prev_addr_postal_code VARCHAR(7), prev_addr_city_code VARCHAR(6), prev_addr_town_code VARCHAR(7), prev_addr_prefecture VARCHAR(4), prev_addr_city_name VARCHAR(12), prev_addr_town VARCHAR(120), prev_addr_house_no VARCHAR(50), prev_addr_apartment VARCHAR(300), prev_addr_country_code VARCHAR(3), prev_addr_country_name VARCHAR(200), prev_addr_overseas VARCHAR(300), next_addr_postal_code VARCHAR(7), next_addr_city_code VARCHAR(6), next_addr_town_code VARCHAR(7), next_addr_prefecture VARCHAR(4), next_addr_city_name VARCHAR(12), next_addr_town VARCHAR(120), next_addr_house_no VARCHAR(50), next_addr_apartment VARCHAR(300), household_head VARCHAR(300), household_no VARCHAR(15), widow_divorced_flag VARCHAR(2), domicile VARCHAR(100), domicile_prefecture VARCHAR(4), domicile_city_name VARCHAR(12), domicile_town VARCHAR(120), domicile_lot_no VARCHAR(50), nhip_payment_special NUMERIC(13), nhip_payment_regular NUMERIC(13), nhip_status VARCHAR(1), care_payment_special NUMERIC(16), care_payment_regular NUMERIC(16), care_status VARCHAR(1), welfare_start_date DATE, welfare_end_date DATE, welfare_suspend_date DATE, welfare_resume_date DATE, welfare_type VARCHAR(2), late_elderly_payment_special NUMERIC(13), late_elderly_payment_regular NUMERIC(13), late_elderly_status VARCHAR(1), disability_cert_type VARCHAR(1), disability_grade VARCHAR(2), disability_cert_issue_date DATE, disability_cert_return_date DATE, disability_cert_reissue_date DATE, mental_disability_expire_date DATE, out_of_resident_tax_flag VARCHAR(1), out_of_resident_tax_city_code VARCHAR(6), out_of_resident_tax_prefecture VARCHAR(6), out_of_resident_tax_city_name VARCHAR(12), out_of_resident_tax_town VARCHAR(120), out_of_resident_tax_house_no VARCHAR(50), out_of_resident_tax_apartment VARCHAR(300), out_of_resident_notice_result VARCHAR(1), other_municipality_tax_flag VARCHAR(1), other_municipality_code VARCHAR(6), other_municipality_prefecture VARCHAR(6), other_municipality_city_name VARCHAR(12), other_municipality_town VARCHAR(120), other_municipality_house_no VARCHAR(50), other_municipality_apartment VARCHAR(300), tax_declaration_send_flag VARCHAR(1), declaration_transfer_term_type VARCHAR(2), declaration_notice_send_flag VARCHAR(1), office_house_tax_declaration_send_flag VARCHAR(1), basic_pension_no VARCHAR(10), city_tax_office_code VARCHAR(5), overseas_period_start DATE, overseas_period_end DATE, memo VARCHAR(2000), is_deleted BOOLEAN NOT NULL, operator_id VARCHAR(10), operated_at DATE, operated_time TIME, PRIMARY KEY (city_code, taxpayer_no, person_hist_no))");
            stmt.execute("CREATE TABLE IF NOT EXISTS individual_resident_tax_ledger (city_code VARCHAR(6) NOT NULL, pre_merge_city_code VARCHAR(6), levy_year INTEGER NOT NULL, tax_year INTEGER NOT NULL, taxpayer_no VARCHAR(15) NOT NULL, assessment_history_no INTEGER NOT NULL, is_latest BOOLEAN, city_ward_code VARCHAR(12), normal_due_date1 DATE, normal_due_date2 DATE, normal_due_date3 DATE, normal_due_date4 DATE, normal_due_date5 DATE, normal_due_date6 DATE, normal_due_date7 DATE, normal_due_date8 DATE, normal_due_date9 DATE, normal_due_date10 DATE, normal_due_date11 DATE, normal_due_date12 DATE, notice_date DATE, normal_amount1 INTEGER, normal_amount2 INTEGER, normal_amount3 INTEGER, normal_amount4 INTEGER, normal_amount5 INTEGER, normal_amount6 INTEGER, normal_amount7 INTEGER, normal_amount8 INTEGER, normal_amount9 INTEGER, normal_amount10 INTEGER, normal_amount11 INTEGER, normal_amount12 INTEGER, normal_paid_amount1 INTEGER, normal_paid_amount2 INTEGER, normal_paid_amount3 INTEGER, normal_paid_amount4 INTEGER, normal_paid_amount5 INTEGER, normal_paid_amount6 INTEGER, normal_paid_amount7 INTEGER, normal_paid_amount8 INTEGER, normal_paid_amount9 INTEGER, normal_paid_amount10 INTEGER, normal_paid_amount11 INTEGER, normal_paid_amount12 INTEGER, normal_sp_annual_amount1 INTEGER, normal_sp_annual_amount2 INTEGER, normal_sp_annual_amount3 INTEGER, normal_sp_annual_amount4 INTEGER, normal_sp_annual_amount5 INTEGER, normal_sp_annual_amount6 INTEGER, normal_sp_annual_amount7 INTEGER, normal_sp_annual_amount8 INTEGER, normal_sp_annual_amount9 INTEGER, normal_sp_annual_amount10 INTEGER, normal_sp_annual_amount11 INTEGER, normal_sp_annual_amount12 INTEGER, notice_no VARCHAR(20), special_agent_no1 VARCHAR(12), special_agent_no2 VARCHAR(12), special_agent_no3 VARCHAR(12), special_agent_no4 VARCHAR(12), special_agent_no5 VARCHAR(12), special_agent_no6 VARCHAR(12), special_agent_no7 VARCHAR(12), special_agent_no8 VARCHAR(12), special_agent_no9 VARCHAR(12), special_agent_no10 VARCHAR(12), special_agent_no11 VARCHAR(12), special_agent_no12 VARCHAR(12), special_amount1 INTEGER, special_amount2 INTEGER, special_amount3 INTEGER, special_amount4 INTEGER, special_amount5 INTEGER, special_amount6 INTEGER, special_amount7 INTEGER, special_amount8 INTEGER, special_amount9 INTEGER, special_amount10 INTEGER, special_amount11 INTEGER, special_amount12 INTEGER, special_paid_amount1 INTEGER, special_paid_amount2 INTEGER, special_paid_amount3 INTEGER, special_paid_amount4 INTEGER, special_paid_amount5 INTEGER, special_paid_amount6 INTEGER, special_paid_amount7 INTEGER, special_paid_amount8 INTEGER, special_paid_amount9 INTEGER, special_paid_amount10 INTEGER, special_paid_amount11 INTEGER, special_paid_amount12 INTEGER, nencho_due_date_apr DATE, nencho_due_date_jun DATE, nencho_due_date_aug DATE, nencho_due_date_oct DATE, nencho_due_date_dec DATE, nencho_due_date_feb DATE, nencho_amount_apr INTEGER, nencho_amount_jun INTEGER, nencho_amount_aug INTEGER, nencho_amount_oct INTEGER, nencho_amount_dec INTEGER, nencho_amount_feb INTEGER, next_nencho_amount_apr INTEGER, next_nencho_amount_jun INTEGER, next_nencho_amount_aug INTEGER, is_deleted BOOLEAN NOT NULL, operator_id VARCHAR(10), operated_at DATE, operated_time TIME, PRIMARY KEY (city_code, levy_year, tax_year, taxpayer_no, assessment_history_no))");
            stmt.execute("INSERT INTO taxpayer_info VALUES ('131016','001',2024,'A00001',1,true,'151000','0','01','1','1000001','東京都千代田区霞が関1-1-1','131016','0001','東京都','千代田区','霞が関','1-1-1','霞が関マンション101','ｻﾄｳ ﾀﾛｳ','ｻﾄｳ','ﾀﾛｳ','佐藤 太郎','佐藤','太郎','1','1980-01-01','01','02','03','04',NULL,NULL,NULL,NULL,'T61','01','01','001','2028-05-31','1','JPN','日本','1','2000-04-01','2022-01-01','2022-01-02','01','2022-03-01','2022-02-10','2022-03-10','2022-04-01','2022-04-10','21','123456789012','1000014','131016','0001','東京都','千代田区','霞が関','1-1-1','霞が関アパート201','265','アメリカ合衆国','1234 APPLE STREET #101 SEATTLE WA','1010001','131016','0002','東京都','新宿区','西新宿','2-2-2','西新宿アパート202','日の丸 一郎','12345678913','01','東京都千代田区霞が関二丁目１番地','東京都','千代田区','霞が関二丁目','１番地',1000000,1000000,'1',2000000,2000000,'1','2022-01-01','2023-01-01','2022-06-01','2022-06-15','01',1000000,1000000,'1','1','01','2022-01-01','2022-02-01','2022-03-01','2022-04-01','1','131016','東京都','千代田区','霞が関二丁目','１番地','霞が関二丁目アパート301','1','1','131016','東京都','千代田区','霞が関二丁目','１番地','霞が関二丁目アパート302','1','01','1','1','1234567890','99999','2020-04-01','2021-03-01','メモサンプル',false,'admin','2024-07-24','09:00:00')");
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
