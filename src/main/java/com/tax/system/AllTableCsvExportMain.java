package com.tax.system;

public class AllTableCsvExportMain {
    public static void main(String[] args) {
        System.out.println("=== 全テーブルCSVエクスポートバッチ処理開始 ===");

        // パラメータファイルからcityCodeとoutputDirを取得
        java.util.Properties params = new java.util.Properties();
        try (java.io.FileReader reader = new java.io.FileReader("src/main/resources/export_params.txt")) {
            params.load(reader);
        } catch (Exception e) {
            System.err.println("パラメータファイル読み込みエラー: " + e.getMessage());
            return;
        }
        String cityCode = params.getProperty("city_code", "131016");
        String outputDir = params.getProperty("output_dir", "./output");

        // try-with-resourcesでConnectionを自動クローズ
        try (java.sql.Connection conn = DatabaseConnection.getConnection()) {
            AllTableCsvExportBatch batch = new AllTableCsvExportBatch(conn);
            batch.exportAll(cityCode, outputDir);
            System.out.println("=== 全テーブルCSVエクスポート完了 ===");
        } catch (Exception e) {
            System.err.println("バッチ処理エラー: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
