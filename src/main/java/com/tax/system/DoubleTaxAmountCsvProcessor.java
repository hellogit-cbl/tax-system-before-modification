package com.tax.system;

import java.io.*;
import java.math.BigDecimal;

public class DoubleTaxAmountCsvProcessor {
    public static void main(String[] args) {
        // 入出力ファイル名（引数で指定も可）
        String inputFile = "./output/tax_assessment_export.csv";
        String outputFile = "./output/tax_assessment_export_double.csv";

        // ファイル読み込み・書き出し
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    // ヘッダー行はそのまま出力
                    writer.write(line);
                    writer.newLine();
                    isFirstLine = false;
                    continue;
                }
                // CSVをカンマで分割
                String[] fields = line.split(",", -1);

                if (fields.length >= 8) {
                    // 5番目の要素が税額（インデックスは5）
                    try {
                        BigDecimal originalTax = new BigDecimal(fields[5]);
                        BigDecimal doubledTax = originalTax.multiply(new BigDecimal(2));
                        fields[5] = doubledTax.toPlainString();
                    } catch (NumberFormatException e) {
                        System.err.println("税額の数値変換エラー: " + fields[5]);
                    }
                }
                // 新しいCSV行を出力
                writer.write(String.join(",", fields));
                writer.newLine();
            }
            System.out.println("税額2倍CSV出力完了: " + outputFile);
        } catch (IOException e) {
            System.err.println("ファイル処理エラー: " + e.getMessage());
            e.printStackTrace();
        }
    }
}