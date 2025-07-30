package com.tax.system;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 修正前の税務査定クラス（行政区コードなし）
 */
public class TaxAssessment {
    private Long id;
    private String municipalityCode;        // 自治体コードのみ
    private Integer assessmentYear;
    private String individualNumber;
    private Integer historyNumber;
    private String taxpayerName;
    private BigDecimal taxAmount;
    private LocalDate taxNoticeIssueDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // コンストラクタ
    public TaxAssessment() {}

    public TaxAssessment(String municipalityCode, Integer assessmentYear, 
                        String individualNumber, String taxpayerName, BigDecimal taxAmount) {
        this.municipalityCode = municipalityCode;
        this.assessmentYear = assessmentYear;
        this.individualNumber = individualNumber;
        this.taxpayerName = taxpayerName;
        this.taxAmount = taxAmount;
        this.historyNumber = 1;
        this.status = "ACTIVE";
    }

    // Getter/Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMunicipalityCode() { return municipalityCode; }
    public void setMunicipalityCode(String municipalityCode) { 
        this.municipalityCode = municipalityCode; 
    }

    public Integer getAssessmentYear() { return assessmentYear; }
    public void setAssessmentYear(Integer assessmentYear) { 
        this.assessmentYear = assessmentYear; 
    }

    public String getIndividualNumber() { return individualNumber; }
    public void setIndividualNumber(String individualNumber) { 
        this.individualNumber = individualNumber; 
    }

    public Integer getHistoryNumber() { return historyNumber; }
    public void setHistoryNumber(Integer historyNumber) { 
        this.historyNumber = historyNumber; 
    }

    public String getTaxpayerName() { return taxpayerName; }
    public void setTaxpayerName(String taxpayerName) { 
        this.taxpayerName = taxpayerName; 
    }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { 
        this.taxAmount = taxAmount; 
    }

    public LocalDate getTaxNoticeIssueDate() { return taxNoticeIssueDate; }
    public void setTaxNoticeIssueDate(LocalDate taxNoticeIssueDate) { 
        this.taxNoticeIssueDate = taxNoticeIssueDate; 
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { 
        this.updatedAt = updatedAt; 
    }

    @Override
    public String toString() {
        return "TaxAssessment{" +
                "municipalityCode='" + municipalityCode + '\'' +
                ", assessmentYear=" + assessmentYear +
                ", individualNumber='" + individualNumber + '\'' +
                ", taxpayerName='" + taxpayerName + '\'' +
                ", taxAmount=" + taxAmount +
                '}';
    }
}