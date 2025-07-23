package com.tax.system;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 納税義務者情報テーブルからデータを抽出し、CSVファイルを作成するクラス。
 */
public class TaxpayerInfoCsvExporter {
    private final Connection connection;

    public TaxpayerInfoCsvExporter(Connection connection) {
        this.connection = connection;
    }

    /**
     * 納税義務者情報テーブルから全データを抽出し、指定したCSVファイルに出力します。
     * @param csvFilePath 出力先CSVファイルパス
     * @throws SQLException
     * @throws IOException
     */
    public void exportToCsv(String cityCode, String csvFilePath) throws SQLException, IOException {
        String columns = "city_code,pre_merge_city_code,tax_year,taxpayer_no,person_hist_no,is_latest,city_ward_code,undeclared_flag,priority_tax_data_type,resident_category,addr_postal_code,addr_full,addr_city_code,addr_town_code,addr_prefecture,addr_city_name,addr_town,addr_house_no,addr_apartment,name_kana,last_name_kana,first_name_kana,name,last_name,first_name,gender,birth_date,relation_code1,relation_code2,relation_code3,relation_code4,name_foreign_kanji,name_foreign_roman,alias_name,alias_kana,visa_status,visa_period_year,visa_period_month,visa_period_day,visa_expiry_date,name_priority_type,nationality_code,nationality_name,resident_status,resident_since,move_date,move_notice_date,move_reason,move_out_planned_date,move_out_notice_date,move_out_confirmed_date,removal_notice_date,removal_move_date,removal_reason,my_number,prev_addr_postal_code,prev_addr_city_code,prev_addr_town_code,prev_addr_prefecture,prev_addr_city_name,prev_addr_town,prev_addr_house_no,prev_addr_apartment,prev_addr_country_code,prev_addr_country_name,prev_addr_overseas,next_addr_postal_code,next_addr_city_code,next_addr_town_code,next_addr_prefecture,next_addr_city_name,next_addr_town,next_addr_house_no,next_addr_apartment,household_head,household_no,widow_divorced_flag,domicile,domicile_prefecture,domicile_city_name,domicile_town,domicile_lot_no,nhip_payment_special,nhip_payment_regular,nhip_status,care_payment_special,care_payment_regular,care_status,welfare_start_date,welfare_end_date,welfare_suspend_date,welfare_resume_date,welfare_type,late_elderly_payment_special,late_elderly_payment_regular,late_elderly_status,disability_cert_type,disability_grade,disability_cert_issue_date,disability_cert_return_date,disability_cert_reissue_date,mental_disability_expire_date,out_of_resident_tax_flag,out_of_resident_tax_city_code,out_of_resident_tax_prefecture,out_of_resident_tax_city_name,out_of_resident_tax_town,out_of_resident_tax_house_no,out_of_resident_tax_apartment,out_of_resident_notice_result,other_municipality_tax_flag,other_municipality_code,other_municipality_prefecture,other_municipality_city_name,other_municipality_town,other_municipality_house_no,other_municipality_apartment,tax_declaration_send_flag,declaration_transfer_term_type,declaration_notice_send_flag,office_house_tax_declaration_send_flag,basic_pension_no,city_tax_office_code,overseas_period_start,overseas_period_end,memo,is_deleted,operator_id,operated_at,operated_time";
        String sql = "SELECT " + columns.replaceAll("([a-zA-Z0-9_]+)", "\"$1\"") + " FROM taxpayer_info WHERE city_code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             FileWriter writer = new FileWriter(csvFilePath)) {
            ps.setString(1, cityCode);
            try (ResultSet rs = ps.executeQuery()) {
                // ヘッダー出力（英語カラム名）
                writer.write(columns + "\n");
                // データ出力
                while (rs.next()) {
                    StringBuilder line = new StringBuilder();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        Object value = rs.getObject(i);
                        if (value != null) {
                            String str = value.toString();
                            // CSVエスケープ
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
