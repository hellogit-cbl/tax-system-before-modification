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
        String columns = "city_code,tax_year,taxpayer_no,assessment_history_no,city_ward_code,is_latest,collection_type,tax_exemption_type,forest_env_tax_exemption_type,pension_type,non_taxable_reason_type,blue_white_type,spouse_special_type,self_special_type,change_reason,change_date,tax_notice_send_date,annual_tax_amount,is_deleted,operated_at";
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
