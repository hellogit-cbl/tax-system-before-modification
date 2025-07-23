package com.tax.system;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * individual_resident_tax_assessmentテーブルからcity_codeで抽出しCSV出力するクラス。
 */
public class IndividualResidentTaxAssessmentCsvExporter {
    private final Connection connection;

    public IndividualResidentTaxAssessmentCsvExporter(Connection connection) {
        this.connection = connection;
    }

    public void exportToCsv(String cityCode, String csvFilePath) throws SQLException, IOException {
        String columns = "city_code,pre_merge_city_code,tax_year,taxpayer_no,assessment_history_no,is_latest,city_ward_code,special_collection_agent_no,collection_type,tax_exemption_type,forest_env_tax_exemption_type,forced_tax_exemption_type,pension_type,nencho_continue_type,non_taxable_reason_type,pension_suspend_type,next_year_provisional_suspend_type,blue_white_type,spouse_special_type,self_special_type,collective_collection_type,change_reason,change_date,correction_date,notice_date,notice_reason,tax_notice_send_date,change_apply_month,income_tax_filed_flag,resident_tax_filed_flag,income_tax_filing_date,spouse_deduction_type,dependent_deduction_type,self_equivalent_spouse_type,self_disability_type,self_widow_type,self_working_student_type,self_minor_dependent_type,self_minor_type,self_old_type,jan1_divorce_remarriage_type,total_dependents,ordinary_dependents,special_dependents,elderly_dependents,cohabiting_elderly_dependents,minor_dependents,total_disabled_dependents,ordinary_disabled_dependents,special_disabled_dependents,cohabiting_special_disabled_dependents,overseas_dependents,spouse_overseas_flag,other_special_dependents,spouse_deduction_amount,other_deduction_amount,income_adjustment_deduction_type,self_medication_deduction_flag,special_acquisition_type,housing_loan_deduction_count,housing_loan_deduction_limit_flag,housing_loan_deduction_start_date1,housing_loan_deduction_type1,housing_loan_deduction_start_date2,housing_loan_deduction_type2,housing_loan_deduction_note,foreign_tax_credit_room,foreign_tax_credit_limit,annual_tax_amount,ordinary_collection_annual_tax_amount,special_collection_annual_tax_amount,nencho_annual_tax_amount,prepayment_flag,prepayment_date,prepayment_amount,deduction_shortage_amount,reduction_type,reduction_rate,reduction_start_month,reduction_start_term,reduction_decision_notice_date,forest_env_tax_exemption_type2,forest_env_tax_exemption_start_month,forest_env_tax_exemption_start_term,forest_env_tax_exemption_decision_date,is_deleted,operator_id,operated_at,operated_time";
        String sql = "SELECT " + columns.replaceAll("([a-zA-Z0-9_]+)", "\"$1\"") + " FROM individual_resident_tax_assessment WHERE city_code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             FileWriter writer = new FileWriter(csvFilePath)) {
            ps.setString(1, cityCode);
            try (ResultSet rs = ps.executeQuery()) {
                writer.write(columns + "\n");
                while (rs.next()) {
                    StringBuilder line = new StringBuilder();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        Object value = rs.getObject(i);
                        if (value != null) {
                            String str = value.toString();
                            if (str.contains(",") || str.contains("\"") || str.contains("\n") || str.contains("\r")) {
                                str = '"' + str.replace("\"", "\"\"") + '"';
                            }
                            line.append(str);
                        }
                        if (i < rs.getMetaData().getColumnCount()) {
                            line.append(",");
                        }
                    }
                    line.append("\n");
                    writer.write(line.toString());
                }
            }
        }
    }
}
