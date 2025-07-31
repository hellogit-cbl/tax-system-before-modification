package com.tax.system;

public class AllTableCsvExportMain {
    public static void main(String[] args) {
        System.out.println("=== 全テーブルCSVエクスポートバッチ処理開始 ===");

        String cityCode = "131016"; // 対象のcity_code
        String outputDir = "./output";

        // try-with-resourcesでConnectionを自動クローズ
        // POSTGRESQLのjdbcドライバを使用して接続
        // ここではDatabaseConnectionクラスを仮定しています。実際の接
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
