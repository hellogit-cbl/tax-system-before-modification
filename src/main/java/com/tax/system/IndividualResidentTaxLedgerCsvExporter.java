package com.tax.system;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * individual_resident_tax_ledgerテーブルからcity_codeで抽出しCSV出力するクラス。
 */
public class IndividualResidentTaxLedgerCsvExporter {
    private final Connection connection;

    public IndividualResidentTaxLedgerCsvExporter(Connection connection) {
        this.connection = connection;
    }

    public void exportToCsv(String cityCode, String csvFilePath) throws SQLException, IOException {
        String columns = "city_code,pre_merge_city_code,levy_year,tax_year,taxpayer_no,assessment_history_no,is_latest,city_ward_code,normal_due_date1,normal_due_date2,normal_due_date3,normal_due_date4,normal_due_date5,normal_due_date6,normal_due_date7,normal_due_date8,normal_due_date9,normal_due_date10,normal_due_date11,normal_due_date12,notice_date,normal_amount1,normal_amount2,normal_amount3,normal_amount4,normal_amount5,normal_amount6,normal_amount7,normal_amount8,normal_amount9,normal_amount10,normal_amount11,normal_amount12,normal_paid_amount1,normal_paid_amount2,normal_paid_amount3,normal_paid_amount4,normal_paid_amount5,normal_paid_amount6,normal_paid_amount7,normal_paid_amount8,normal_paid_amount9,normal_paid_amount10,normal_paid_amount11,normal_paid_amount12,normal_sp_annual_amount1,normal_sp_annual_amount2,normal_sp_annual_amount3,normal_sp_annual_amount4,normal_sp_annual_amount5,normal_sp_annual_amount6,normal_sp_annual_amount7,normal_sp_annual_amount8,normal_sp_annual_amount9,normal_sp_annual_amount10,normal_sp_annual_amount11,normal_sp_annual_amount12,notice_no,special_agent_no1,special_agent_no2,special_agent_no3,special_agent_no4,special_agent_no5,special_agent_no6,special_agent_no7,special_agent_no8,special_agent_no9,special_agent_no10,special_agent_no11,special_agent_no12,special_amount1,special_amount2,special_amount3,special_amount4,special_amount5,special_amount6,special_amount7,special_amount8,special_amount9,special_amount10,special_amount11,special_amount12,special_paid_amount1,special_paid_amount2,special_paid_amount3,special_paid_amount4,special_paid_amount5,special_paid_amount6,special_paid_amount7,special_paid_amount8,special_paid_amount9,special_paid_amount10,special_paid_amount11,special_paid_amount12,nencho_due_date_apr,nencho_due_date_jun,nencho_due_date_aug,nencho_due_date_oct,nencho_due_date_dec,nencho_due_date_feb,nencho_amount_apr,nencho_amount_jun,nencho_amount_aug,nencho_amount_oct,nencho_amount_dec,nencho_amount_feb,next_nencho_amount_apr,next_nencho_amount_jun,next_nencho_amount_aug,is_deleted,operator_id,operated_at,operated_time";
        String sql = "SELECT " + columns.replaceAll("([a-zA-Z0-9_]+)", "\"$1\"") + " FROM individual_resident_tax_ledger WHERE city_code = ?";
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
