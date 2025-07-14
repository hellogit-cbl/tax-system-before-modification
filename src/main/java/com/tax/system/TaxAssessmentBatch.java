package com.tax.system;

import java.time.LocalDate;
import java.util.List;

/**
 * 修正前の税務システムバッチ処理
 */
public class TaxAssessmentBatch {

    public static void main(String[] args) {
        System.out.println("=== 修正前 税務システムバッチ処理開始 ===");

        // パラメータ設定
        String municipalityCode = "001";  // 自治体コードのみ
        Integer assessmentYear = 2024;
        String outputDirectory = "./output";

        try {
            // Phase 1: データ抽出
            System.out.println("Phase 1: データ抽出開始");
            DataExtractor extractor = new DataExtractor();
            List<TaxAssessment> assessments = extractor.extractTaxData(municipalityCode, assessmentYear);

            // Phase 2: データ加工・計算
            System.out.println("Phase 2: データ加工・計算開始");
            List<TaxAssessment> uniqueAssessments = extractor.removeDuplicates(assessments);

            // 納税通知形式で編集（簡単な例）
            for (TaxAssessment assessment : uniqueAssessments) {
                if (assessment.getTaxNoticeIssueDate() == null) {
                    assessment.setTaxNoticeIssueDate(LocalDate.now());
                }
            }

            // CSV出力（修正前：単一ファイル、単一ディレクトリ）
            extractor.exportToCsv(uniqueAssessments, outputDirectory);

            // Phase 3: データロード
            System.out.println("Phase 3: データロード開始");
            DataLoader loader = new DataLoader();
            loader.updateTaxNoticeIssueDate(municipalityCode, assessmentYear, LocalDate.now());

            System.out.println("=== 修正前 税務システムバッチ処理完了 ===");

        } catch (Exception e) {
            System.err.println("バッチ処理エラー: " + e.getMessage());
            e.printStackTrace();
        }
    }
}