package com.tax.system;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;

/**
 * 各テーブルのCSVエクスポートを一括実行するバッチクラス。
 */
public class AllTableCsvExportBatch {
    private final Connection connection;

    public AllTableCsvExportBatch(Connection connection) {
        this.connection = connection;
    }

    /**
     * 指定city_codeの各テーブルデータを一括でCSV出力します。
     * @param cityCode 対象のcity_code
     * @param outputDir 出力先ディレクトリ（末尾スラッシュ不要）
     * @throws SQLException
     * @throws IOException
     */
    public void exportAll(String cityCode, String outputDir) throws SQLException, IOException {
        new TaxpayerInfoCsvExporter(connection).exportToCsv(cityCode, outputDir + "/taxpayer_info.csv");
        new IndividualResidentTaxAssessmentCsvExporter(connection).exportToCsv(cityCode, outputDir + "/individual_resident_tax_assessment.csv");
        new IndividualResidentTaxLedgerCsvExporter(connection).exportToCsv(cityCode, outputDir + "/individual_resident_tax_ledger.csv");
        new IndividualResidentTaxIncomeCsvExporter(connection).exportToCsv(cityCode, outputDir + "/individual_resident_tax_income.csv");
        new IndividualResidentTaxDeductionCsvExporter(connection).exportToCsv(cityCode, outputDir + "/individual_resident_tax_deduction.csv");
    }
}
