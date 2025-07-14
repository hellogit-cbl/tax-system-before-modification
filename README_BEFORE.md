# Java税務システム（修正前版）

## 概要
このシステムは修正前の仕様で、自治体コードのみでデータ抽出を行います。

## 修正前の特徴
- ✅ 自治体コード（municipality_code）のみでデータ抽出
- ✅ 行政区コード（administrative_district_code）は存在しない
- ✅ 単一ファイル出力
- ✅ 単一ディレクトリ出力

## セットアップ手順

### 1. PostgreSQL起動
```bash
docker-compose up -d
```

### 2. ビルド
```bash
mvn clean compile
```

### 3. 実行
```bash
mvn exec:java -Dexec.mainClass="com.tax.system.TaxAssessmentBatch"
```

## データ構造（修正前）

### テーブル定義
```sql
CREATE TABLE tax_assessment (
    id SERIAL PRIMARY KEY,
    municipality_code VARCHAR(6) NOT NULL,    -- 自治体コードのみ
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
```

## 処理フロー（修正前）

1. **データ抽出**: 自治体コード + 賦課年度で抽出
2. **データ処理**: 重複除去（同一個人番号で最新履歴のみ）
3. **ファイル出力**: 単一CSVファイル出力
4. **データ更新**: 納税通知発行年月日を更新

## 出力ファイル
- `./output/tax_assessment_export.csv`

## テストデータ
- 自治体コード001: 5件のデータ
- 自治体コード002: 4件のデータ
- 履歴番号重複あり（最新のみ抽出）

## 次の修正予定
1. 行政区コード追加
2. 行政区別ファイル出力
3. 行政区別ディレクトリ出力
4. データロード時の行政区コード追加

## 技術スタック
- Java 24
- PostgreSQL 16
- Maven 3.9.x
- Docker Compose
- TestContainers

## 開発環境
- Windows 11 Home
- Eclipse 2024-03 または IntelliJ IDEA
- Git 2.43.x
