-- 修正前のテストデータ（行政区コードなし）
INSERT INTO tax_assessment 
(municipality_code, assessment_year, individual_number, history_number, 
 taxpayer_name, tax_amount, status) 
VALUES 
-- 自治体コード001のデータ（行政区A相当）
('001', 2024, '123456789001', 1, '田中太郎', 150000.00, 'ACTIVE'),
('001', 2024, '123456789002', 1, '佐藤花子', 120000.00, 'ACTIVE'),
('001', 2024, '123456789003', 1, '鈴木一郎', 180000.00, 'ACTIVE'),
('001', 2024, '123456789003', 2, '鈴木一郎', 185000.00, 'ACTIVE'), -- 履歴番号2
('001', 2024, '123456789004', 1, '高橋美咲', 95000.00, 'ACTIVE'),

-- 自治体コード002のデータ（行政区B相当）  
('002', 2024, '123456789005', 1, '山田次郎', 110000.00, 'ACTIVE'),
('002', 2024, '123456789006', 1, '田村由美', 140000.00, 'ACTIVE'),
('002', 2024, '123456789007', 1, '渡辺健太', 160000.00, 'ACTIVE'),
('002', 2024, '123456789008', 1, '中村真理', 130000.00, 'ACTIVE'),

-- 2023年のデータ
('001', 2023, '123456789001', 1, '田中太郎', 145000.00, 'ACTIVE'),
('001', 2023, '123456789002', 1, '佐藤花子', 115000.00, 'ACTIVE'),
('002', 2023, '123456789005', 1, '山田次郎', 105000.00, 'ACTIVE');

INSERT INTO taxpayer_info (
    city_code, pre_merge_city_code, tax_year, taxpayer_no, person_hist_no, is_latest, city_ward_code,
    undeclared_flag, priority_tax_data_type, resident_category, addr_postal_code, addr_full, addr_city_code, addr_town_code,
    addr_prefecture, addr_city_name, addr_town, addr_house_no, addr_apartment, name_kana, last_name_kana, first_name_kana,
    name, last_name, first_name, gender, birth_date, relation_code1, relation_code2, relation_code3, relation_code4,
    name_foreign_kanji, name_foreign_roman, alias_name, alias_kana, visa_status, visa_period_year, visa_period_month,
    visa_period_day, visa_expiry_date, name_priority_type, nationality_code, nationality_name, resident_status, resident_since,
    move_date, move_notice_date, move_reason, move_out_planned_date, move_out_notice_date, move_out_confirmed_date,
    removal_notice_date, removal_move_date, removal_reason, my_number, prev_addr_postal_code, prev_addr_city_code,
    prev_addr_town_code, prev_addr_prefecture, prev_addr_city_name, prev_addr_town, prev_addr_house_no, prev_addr_apartment,
    prev_addr_country_code, prev_addr_country_name, prev_addr_overseas, next_addr_postal_code, next_addr_city_code,
    next_addr_town_code, next_addr_prefecture, next_addr_city_name, next_addr_town, next_addr_house_no, next_addr_apartment,
    household_head, household_no, widow_divorced_flag, domicile, domicile_prefecture, domicile_city_name, domicile_town,
    domicile_lot_no, nhip_payment_special, nhip_payment_regular, nhip_status, care_payment_special, care_payment_regular,
    care_status, welfare_start_date, welfare_end_date, welfare_suspend_date, welfare_resume_date, welfare_type,
    late_elderly_payment_special, late_elderly_payment_regular, late_elderly_status, disability_cert_type, disability_grade,
    disability_cert_issue_date, disability_cert_return_date, disability_cert_reissue_date, mental_disability_expire_date,
    out_of_resident_tax_flag, out_of_resident_tax_city_code, out_of_resident_tax_prefecture, out_of_resident_tax_city_name,
    out_of_resident_tax_town, out_of_resident_tax_house_no, out_of_resident_tax_apartment, out_of_resident_notice_result,
    other_municipality_tax_flag, other_municipality_code, other_municipality_prefecture, other_municipality_city_name,
    other_municipality_town, other_municipality_house_no, other_municipality_apartment, tax_declaration_send_flag,
    declaration_transfer_term_type, declaration_notice_send_flag, office_house_tax_declaration_send_flag, basic_pension_no,
    city_tax_office_code, overseas_period_start, overseas_period_end, memo, is_deleted, operator_id, operated_at, operated_time
) VALUES
-- 1件目
('131016', NULL, 2024, 'A00001', 1, TRUE, '151000', '0', '01', '1', '1000001', '東京都千代田区霞が関1-1-1', '131016', '0001',
'東京都', '千代田区', '霞が関', '1-1-1', '霞が関マンション101', 'ｻﾄｳ ﾀﾛｳ', 'ｻﾄｳ', 'ﾀﾛｳ', '佐藤 太郎', '佐藤', '太郎', '1', '1980-01-01',
'01','02','03','04', NULL, NULL, NULL, NULL, 'T61', '01', '01', '001', '2028-05-31', '1', 'JPN', '日本', '1', '2000-04-01',
'2022-01-01', '2022-01-02', '01', '2022-03-01', '2022-02-10', '2022-03-10', '2022-04-01', '2022-04-10', '21', '123456789012',
'1000014', '131016', '0001', '東京都', '千代田区', '霞が関', '1-1-1', '霞が関アパート201', '265', 'アメリカ合衆国', '1234 APPLE STREET #101 SEATTLE WA',
'1010001', '131016', '0002', '東京都', '新宿区', '西新宿', '2-2-2', '西新宿アパート202', '日の丸 一郎', '12345678913', '01', '東京都千代田区霞が関二丁目1番地',
'東京都', '千代田区', '霞が関二丁目', '1番地', 1000000, 1000000, '1', 2000000, 2000000, '1', '2022-01-01', '2023-01-01',
'2022-06-01', '2022-06-15', '01', 1000000, 1000000, '1', '1', '01', '2022-01-01', '2022-02-01', '2022-03-01', '2022-04-01',
'1', '131016', '東京都', '千代田区', '霞が関二丁目', '1番地', '霞が関二丁目アパート301', '1', '1', '131016', '東京都', '千代田区', '霞が関二丁目',
'1番地', '霞が関二丁目アパート302', '1', '01', '1', '1', '1234567890', '99999', '2020-04-01', '2021-03-01', 'メモサンプル', FALSE, 'admin', '2024-07-24', '09:00:00'
),
-- 2件目
('131016', NULL, 2024, 'A00002', 1, TRUE, '151001', '1', '02', '2', '1000002', '東京都千代田区永田町2-2-2', '131016', '0002',
'東京都', '千代田区', '永田町', '2-2-2', '永田町ハイツ2F', 'ﾔﾏﾀﾞ ｼﾞﾛｳ', 'ﾔﾏﾀﾞ', 'ｼﾞﾛｳ', '山田 次郎', '山田', '次郎', '2', '1985-02-02',
'11','12','13','14', NULL, NULL, NULL, NULL, 'T62', '02', '02', '002', '2029-06-30', '2', 'USA', 'アメリカ', '2', '2005-05-01',
'2021-02-02', '2021-02-03', '02', '2021-04-01', '2021-03-10', '2021-04-10', '2021-05-01', '2021-05-10', '22', '123456789013',
'1000015', '131016', '0002', '東京都', '新宿区', '歌舞伎町', '3-3-3', '歌舞伎町ハイツ301', '265', 'アメリカ合衆国', '5678 ORANGE STREET #202 TOKYO',
'1010002', '131016', '0003', '東京都', '新宿区', '歌舞伎町', '3-3-3', '歌舞伎町ハイツ302', '日の丸 二郎', '12345678914', '02', '東京都新宿区西新宿三丁目2番地',
'東京都', '新宿区', '西新宿三丁目', '2番地', 2000000, 2000000, '2', 3000000, 3000000, '2', '2021-02-01', '2022-02-01',
'2021-07-01', '2021-07-15', '02', 2000000, 2000000, '2', '2', '02', '2021-02-01', '2021-03-01', '2021-04-01', '2021-05-01',
'2', '131016', '東京都', '新宿区', '歌舞伎町', '3番地', '歌舞伎町ハイツ303', '2', '2', '131016', '東京都', '新宿区', '歌舞伎町',
'3番地', '歌舞伎町ハイツ304', '2', '02', '2', '2', '2345678901', '88888', '2021-04-01', '2022-03-01', 'メモサンプル2', FALSE, 'user1', '2024-07-24', '09:01:00'
),
-- 3件目
('131016', NULL, 2025, 'A00003', 1, TRUE, '151002', '0', '03', '1', '1000003', '東京都新宿区西新宿3-3-3', '131016', '0003',
'東京都', '新宿区', '西新宿', '3-3-3', '西新宿グリーンハイツ1F', 'ﾀﾅｶ ｻﾌﾞﾛｳ', 'ﾀﾅｶ', 'ｻﾌﾞﾛｳ', '田中 三郎', '田中', '三郎', '1', '1990-03-03',
'21','22','23','24', NULL, NULL, NULL, NULL, 'T63', '03', '03', '003', '2030-07-31', '3', 'CHN', '中国', '1', '2010-06-01',
'2023-03-03', '2023-03-04', '03', '2023-05-01', '2023-04-10', '2023-05-10', '2023-06-01', '2023-06-10', '23', '123456789014',
'1000016', '131016', '0003', '東京都', '渋谷区', '道玄坂', '4-4-4', '道玄坂アパート401', '156', '中国', '91011 PINE STREET #303 CHINA',
'1010003', '131016', '0004', '東京都', '渋谷区', '道玄坂', '4-4-4', '道玄坂アパート402', '日の丸 三郎', '12345678915', '03', '東京都渋谷区道玄坂四丁目3番地',
'東京都', '渋谷区', '道玄坂四丁目', '3番地', 3000000, 3000000, '1', 4000000, 4000000, '1', '2023-03-01', '2024-03-01',
'2023-08-01', '2023-08-15', '03', 3000000, 3000000, '1', '1', '03', '2023-03-01', '2023-04-01', '2023-05-01', '2023-06-01',
'1', '131016', '東京都', '渋谷区', '道玄坂四丁目', '4番地', '道玄坂アパート403', '1', '1', '131016', '東京都', '渋谷区', '道玄坂四丁目',
'4番地', '道玄坂アパート404', '1', '03', '1', '1', '3456789012', '77777', '2022-04-01', '2023-03-01', 'メモサンプル3', FALSE, 'user2', '2024-07-24', '09:02:00'
),
-- 4件目
('131017', '131018', 2024, 'A00004', 1, TRUE, '151003', '1', '04', '2', '1000004', '東京都港区南青山4-4-4', '131018', '0004',
'東京都', '港区', '南青山', '4-4-4', '南青山グランドタワー12F', 'ｽｽﾞｷ ｼﾛｳ', 'ｽｽﾞｷ', 'ｼﾛｳ', '鈴木 四郎', '鈴木', '四郎', '2', '1995-04-04',
'31','32','33','34', NULL, NULL, NULL, NULL, 'T64', '04', '04', '004', '2031-08-31', '4', 'KOR', '韓国', '2', '2015-07-01',
'2022-04-04', '2022-04-05', '04', '2022-06-01', '2022-05-10', '2022-06-10', '2022-07-01', '2022-07-10', '24', '123456789015',
'1000017', '131017', '0004', '東京都', '港区', '南青山', '5-5-5', '南青山タワー1201', '392', '韓国', '121314 MAPLE STREET #404 KOREA',
'1010004', '131016', '0005', '東京都', '港区', '南青山', '5-5-5', '南青山タワー1202', '日の丸 四郎', '12345678916', '04', '東京都港区南青山五丁目4番地',
'東京都', '港区', '南青山五丁目', '4番地', 4000000, 4000000, '2', 5000000, 5000000, '2', '2022-04-01', '2023-04-01',
'2022-09-01', '2022-09-15', '04', 4000000, 4000000, '2', '2', '04', '2022-04-01', '2022-05-01', '2022-06-01', '2022-07-01',
'2', '131017', '東京都', '港区', '南青山五丁目', '5番地', '南青山五丁目レジデンス', '2', '2', '131016', '東京都', '港区', '南青山五丁目',
'5番地', '南青山五丁目レジデンス2', '2', '04', '2', '2', '4567890123', '66666', '2023-04-01', '2024-03-01', 'メモサンプル4', FALSE, 'user3', '2024-07-24', '09:03:00'
);
INSERT INTO public.individual_resident_tax_assessment (
    city_code, tax_year, taxpayer_no, assessment_history_no,
    city_ward_code, is_latest, collection_type, tax_exemption_type,
    forest_env_tax_exemption_type, pension_type, non_taxable_reason_type,
    blue_white_type, spouse_special_type, self_special_type,
    change_reason, change_date, tax_notice_send_date,
    annual_tax_amount, is_deleted, operated_at
) VALUES
-- ① 通常のデータ
('131016', 2024, 'A00001', 1, '151000', true, 'A', '1', '1', 'P01', '01', 'B', '1', '1', '101', '2024-04-01', '2024-04-10', 150000, false, '2024-04-11'),

-- ② is_latest = false、異なる年度
('131016', 2023, 'A00002', 1, '151000', false, 'B', '2', '0', 'P02', '02', 'W', '0', '0', '102', '2023-03-15', '2023-03-20', 130000, false, '2023-03-21'),

-- ③ 年税額ゼロ、空項目あり
('131016', 2024, 'A00003', 1, 151001, false, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, false, NULL),

-- ④ 削除済みデータ（is_deleted = true）
('131016', 2024, 'A00004', 1, '151001', true, 'C', '3', '2', 'P03', '03', 'R', '1', '1', '103', '2024-05-01', '2024-05-10', 180000, true, '2024-05-11');

INSERT INTO individual_resident_tax_income (
    city_code, tax_year, taxpayer_no, assessment_history_no, income_code, is_latest, city_ward_code,
    income_amount, is_deleted, operator_id, operated_at, operated_time
) VALUES
('131016', 2024, 'A00001', 1, '101', TRUE, '151000', 4800000, FALSE, 'admin', '2024-07-24', '11:00:00'),
('131016', 2024, 'A00002', 1, '102', TRUE, '151000', 3900000, FALSE, 'user1', '2024-07-24', '11:01:00'),
('131016', 2025, 'A00003', 2, '201', TRUE, '151001', 5200000, FALSE, 'user2', '2024-07-24', '11:02:00'),
('131016', 2025, 'A00004', 2, '202', TRUE, '151001', 4100000, FALSE, 'user3', '2024-07-24', '11:03:00');

INSERT INTO individual_resident_tax_deduction (
    city_code, tax_year, taxpayer_no, assessment_history_no, deduction_code, is_latest, city_ward_code,
    deduction_amount, is_deleted, operator_id, operated_at, operated_time
) VALUES
('131016', 2024, 'A00001', 1, '301', TRUE, '151000', 380000, FALSE, 'admin', '2024-07-24', '11:10:00'),
('131016', 2024, 'A00002', 1, '302', TRUE, '151000', 270000, FALSE, 'user1', '2024-07-24', '11:11:00'),
('131016', 2025, 'A00003', 2, '401', TRUE, '151001', 410000, FALSE, 'user2', '2024-07-24', '11:12:00'),
('131016', 2025, 'A00004', 2, '402', TRUE, '151001', 220000, FALSE, 'user3', '2024-07-24', '11:13:00');

INSERT INTO individual_resident_tax_ledger (
    city_code, levy_year, tax_year, taxpayer_no, assessment_history_no, is_latest, is_deleted, operator_id, operated_at, operated_time
) VALUES
('131016', 2024, 2024, 'A00001', 1, TRUE, FALSE, 'admin', '2024-07-24', '09:15:00'),
('131016', 2024, 2024, 'A00002', 1, TRUE, FALSE, 'admin', '2024-07-24', '09:16:00'),
('131016', 2024, 2024, 'A00003', 1, TRUE, FALSE, 'admin', '2024-07-24', '09:17:00'),
('131016', 2025, 2025, 'A00001', 2, TRUE, FALSE, 'admin', '2024-07-24', '09:18:00');
