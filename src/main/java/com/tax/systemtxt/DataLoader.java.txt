package com.tax.system;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 修正前のデータロードクラス
 */
public class DataLoader {

    /**
     * CSVファイルからデータをロード（修正前）
     */
    public void loadFromCsv(String csvFilePath) {
        String sql = """
            INSERT INTO tax_assessment 
            (municipality_code, assessment_year, individual_number, history_number, 
             taxpayer_name, tax_amount, tax_notice_issue_date, status) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {

            String line;
            boolean isFirstLine = true;
            int count = 0;

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // ヘッダーをスキップ
                }

                String[] fields = line.split(",");
                if (fields.length >= 8) {
                    stmt.setString(1, fields[0]); // municipality_code
                    stmt.setInt(2, Integer.parseInt(fields[1])); // assessment_year
                    stmt.setString(3, fields[2]); // individual_number
                    stmt.setInt(4, Integer.parseInt(fields[3])); // history_number
                    stmt.setString(5, fields[4]); // taxpayer_name
                    stmt.setBigDecimal(6, new BigDecimal(fields[5])); // tax_amount

                    // tax_notice_issue_date
                    if (!fields[6].isEmpty()) {
                        stmt.setDate(7, java.sql.Date.valueOf(LocalDate.parse(fields[6], dateFormatter)));
                    } else {
                        stmt.setDate(7, null);
                    }

                    stmt.setString(8, fields[7]); // status

                    stmt.addBatch();
                    count++;

                    if (count % 1000 == 0) {
                        stmt.executeBatch();
                        System.out.println("処理済み: " + count + "件");
                    }
                }
            }

            stmt.executeBatch();
            System.out.println("データロード完了: " + count + "件");

        } catch (SQLException | IOException e) {
            throw new RuntimeException("データロードエラー", e);
        }
    }

    /**
     * 納税通知発行年月日の更新（修正前）
     */
    public void updateTaxNoticeIssueDate(String municipalityCode, Integer assessmentYear, 
                                       LocalDate issueDate) {
        String sql = """
            UPDATE tax_assessment 
            SET tax_notice_issue_date = ?, updated_at = CURRENT_TIMESTAMP 
            WHERE municipality_code = ? AND assessment_year = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(issueDate));
            stmt.setString(2, municipalityCode);
            stmt.setInt(3, assessmentYear);

            int updatedRows = stmt.executeUpdate();
            System.out.println("納税通知発行年月日更新: " + updatedRows + "件");

        } catch (SQLException e) {
            throw new RuntimeException("更新エラー", e);
        }
    }
}