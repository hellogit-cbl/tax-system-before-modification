-- 修正前のテーブル定義（行政区コードなし）
CREATE TABLE IF NOT EXISTS tax_assessment (
    id SERIAL PRIMARY KEY,
    municipality_code VARCHAR(6) NOT NULL,       -- 自治体コードのみ
    assessment_year INTEGER NOT NULL,
    individual_number VARCHAR(12) NOT NULL,
    history_number INTEGER NOT NULL DEFAULT 1,
    taxpayer_name VARCHAR(100) NOT NULL,
    tax_amount DECIMAL(15,2) NOT NULL,
    tax_notice_issue_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 修正前のインデックス
CREATE INDEX IF NOT EXISTS idx_tax_assessment_municipality_year 
ON tax_assessment (municipality_code, assessment_year);

CREATE INDEX IF NOT EXISTS idx_tax_assessment_individual_history 
ON tax_assessment (individual_number, history_number DESC);

-- 更新日時自動更新トリガー
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_tax_assessment_updated_at
    BEFORE UPDATE ON tax_assessment
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();