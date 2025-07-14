package com.tax.system;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 修正前のデータ抽出クラス（自治体コードのみ）
 */
public class DataExtractor {

    /**
     * 自治体コードと賦課年度で税務データを抽出（修正前）
     */
    public List<TaxAssessment> extractTaxData(String municipalityCode, Integer assessmentYear) {
        List<TaxAssessment> results = new ArrayList<>();

        String sql = """
            SELECT id, municipality_code, assessment_year, individual_number, 
                   history_number, taxpayer_name, tax_amount, tax_notice_issue_date, 
                   status, created_at, updated_at
            FROM tax_assessment 
            WHERE municipality_code = ? AND assessment_year = ?
            ORDER BY municipality_code ASC, assessment_year ASC, history_number DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, municipalityCode);
            stmt.setInt(2, assessmentYear);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TaxAssessment assessment = new TaxAssessment();
                    assessment.setId(rs.getLong("id"));
                    assessment.setMunicipalityCode(rs.getString("municipality_code"));
                    assessment.setAssessmentYear(rs.getInt("assessment_year"));
                    assessment.setIndividualNumber(rs.getString("individual_number"));
                    assessment.setHistoryNumber(rs.getInt("history_number"));
                    assessment.setTaxpayerName(rs.getString("taxpayer_name"));
                    assessment.setTaxAmount(rs.getBigDecimal("tax_amount"));

                    if (rs.getDate("tax_notice_issue_date") != null) {
                        assessment.setTaxNoticeIssueDate(rs.getDate("tax_notice_issue_date").toLocalDate());
                    }

                    assessment.setStatus(rs.getString("status"));
                    assessment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    assessment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

                    results.add(assessment);
                }
            }

            System.out.println("データ抽出完了: " + results.size() + "件");

        } catch (SQLException e) {
            throw new RuntimeException("データ抽出エラー", e);
        }

        return results;
    }

    /**
     * 重複除去（同一個人番号で最新履歴のみ）
     */
    public List<TaxAssessment> removeDuplicates(List<TaxAssessment> assessments) {
        List<TaxAssessment> uniqueList = new ArrayList<>();
        List<String> processedIndividuals = new ArrayList<>();

        for (TaxAssessment assessment : assessments) {
            if (!processedIndividuals.contains(assessment.getIndividualNumber())) {
                uniqueList.add(assessment);
                processedIndividuals.add(assessment.getIndividualNumber());
            }
        }

        System.out.println("重複除去後: " + uniqueList.size() + "件");
        return uniqueList;
    }

    /**
     * CSVファイル出力（修正前：単一ファイル）
     */
    public void exportToCsv(List<TaxAssessment> assessments, String outputDirectory) {
        String fileName = outputDirectory + "/tax_assessment_export.csv";

        try (FileWriter writer = new FileWriter(fileName)) {
            // ヘッダー
            writer.write("自治体コード,賦課年度,個人番号,履歴番号,納税者名,税額,納税通知発行年月日,ステータス\n");

            // データ
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (TaxAssessment assessment : assessments) {
                writer.write(String.format("%s,%d,%s,%d,%s,%.2f,%s,%s\n",
                    assessment.getMunicipalityCode(),
                    assessment.getAssessmentYear(),
                    assessment.getIndividualNumber(),
                    assessment.getHistoryNumber(),
                    assessment.getTaxpayerName(),
                    assessment.getTaxAmount(),
                    assessment.getTaxNoticeIssueDate() != null ? 
                        assessment.getTaxNoticeIssueDate().format(dateFormatter) : "",
                    assessment.getStatus()
                ));
            }

            System.out.println("CSV出力完了: " + fileName);

        } catch (IOException e) {
            throw new RuntimeException("CSV出力エラー", e);
        }
    }
}