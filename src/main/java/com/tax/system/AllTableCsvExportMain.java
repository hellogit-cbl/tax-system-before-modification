package com.tax.system;

// ...existing code...

public class AllTableCsvExportMain {
    public static void main(String[] args) {
        System.out.println("=== 全テーブルCSVエクスポートバッチ処理開始 ===");

        // パラメータ設定例（必要に応じて修正）
        String cityCode = "131016"; // 対象のcity_code
        String outputDir = "./output";
        try {
            AllTableCsvExportBatch batch = new AllTableCsvExportBatch(DatabaseConnection.getConnection());
            batch.exportAll(cityCode, outputDir);
            System.out.println("=== 全テーブルCSVエクスポート完了 ===");
        } catch (Exception e) {
            System.err.println("バッチ処理エラー: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
