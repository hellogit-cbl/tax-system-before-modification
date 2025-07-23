package com.tax.system;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * individual_resident_tax_deductionテーブルからcity_codeで抽出しCSV出力するクラス。
 */
public class IndividualResidentTaxDeductionCsvExporter {
    private final Connection connection;

    public IndividualResidentTaxDeductionCsvExporter(Connection connection) {
        this.connection = connection;
    }

    public void exportToCsv(String cityCode, String csvFilePath) throws SQLException, IOException {
        String columns = "city_code,tax_year,taxpayer_no,assessment_history_no,deduction_code,is_latest,city_ward_code,deduction_amount,is_deleted,operator_id,operated_at,operated_time";
        String sql = "SELECT " + columns.replaceAll("([a-zA-Z0-9_]+)", "\"$1\"") + " FROM individual_resident_tax_deduction WHERE city_code = ?";
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
