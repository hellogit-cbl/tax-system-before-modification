package com.tax.system;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataExtractorTest {

    @Test
    void testRemoveDuplicates() {
        DataExtractor extractor = new DataExtractor();

        // テストデータ作成
        List<TaxAssessment> assessments = new ArrayList<>();

        TaxAssessment assessment1 = new TaxAssessment("001", 2024, "123456789001", "田中太郎", new BigDecimal("150000.00"));
        TaxAssessment assessment2 = new TaxAssessment("001", 2024, "123456789001", "田中太郎", new BigDecimal("155000.00"));
        TaxAssessment assessment3 = new TaxAssessment("001", 2024, "123456789002", "佐藤花子", new BigDecimal("120000.00"));

        assessments.add(assessment1);
        assessments.add(assessment2);
        assessments.add(assessment3);

        // 重複除去テスト
        List<TaxAssessment> uniqueAssessments = extractor.removeDuplicates(assessments);

        assertEquals(2, uniqueAssessments.size());
        assertEquals("123456789001", uniqueAssessments.get(0).getIndividualNumber());
        assertEquals("123456789002", uniqueAssessments.get(1).getIndividualNumber());
    }

    @Test
    void testTaxAssessmentCreation() {
        TaxAssessment assessment = new TaxAssessment("001", 2024, "123456789001", "田中太郎", new BigDecimal("150000.00"));

        assertEquals("001", assessment.getMunicipalityCode());
        assertEquals(2024, assessment.getAssessmentYear());
        assertEquals("123456789001", assessment.getIndividualNumber());
        assertEquals("田中太郎", assessment.getTaxpayerName());
        assertEquals(new BigDecimal("150000.00"), assessment.getTaxAmount());
        assertEquals("ACTIVE", assessment.getStatus());
        assertEquals(1, assessment.getHistoryNumber());
    }
}