package com.tax.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;

public class AllTableCsvExportMain {
    public static void main(String[] args) {
        System.out.println("=== 全テーブルCSVエクスポートバッチ処理開始 ===");

        // パラメータ設定例（必要に応じて修正）
        String cityCode = "131016"; // 対象のcity_code
        String outputDir = "./output";
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "postgres";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            AllTableCsvExportBatch batch = new AllTableCsvExportBatch(conn);
            batch.exportAll(cityCode, outputDir);
            System.out.println("=== 全テーブルCSVエクスポート完了 ===");
        } catch (SQLException | IOException e) {
            System.err.println("バッチ処理エラー: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
