package com.prepcv.pms.dto;

import com.prepcv.pms.entity.DriveStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StudentPlacementDriveResponse {

    private Long driveId;
    private Long companyId;
    private String companyName;

    private String jobTitle;
    private String description;

    private BigDecimal minimumCgpa;
    private BigDecimal minimumTenthPercentage;
    private BigDecimal minimumTwelfthPercentage;

    private LocalDate applicationDeadline;
    private LocalDate driveDate;

    private DriveStatus status;

    private boolean eligible;
    private String eligibilityMessage;

    public StudentPlacementDriveResponse() {
    }

    public StudentPlacementDriveResponse(
            Long driveId,
            Long companyId,
            String companyName,
            String jobTitle,
            String description,
            BigDecimal minimumCgpa,
            BigDecimal minimumTenthPercentage,
            BigDecimal minimumTwelfthPercentage,
            LocalDate applicationDeadline,
            LocalDate driveDate,
            DriveStatus status,
            boolean eligible,
            String eligibilityMessage
    ) {
        this.driveId = driveId;
        this.companyId = companyId;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.description = description;
        this.minimumCgpa = minimumCgpa;
        this.minimumTenthPercentage = minimumTenthPercentage;
        this.minimumTwelfthPercentage = minimumTwelfthPercentage;
        this.applicationDeadline = applicationDeadline;
        this.driveDate = driveDate;
        this.status = status;
        this.eligible = eligible;
        this.eligibilityMessage = eligibilityMessage;
    }

    public Long getDriveId() {
        return driveId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getMinimumCgpa() {
        return minimumCgpa;
    }

    public BigDecimal getMinimumTenthPercentage() {
        return minimumTenthPercentage;
    }

    public BigDecimal getMinimumTwelfthPercentage() {
        return minimumTwelfthPercentage;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public LocalDate getDriveDate() {
        return driveDate;
    }

    public DriveStatus getStatus() {
        return status;
    }

    public boolean isEligible() {
        return eligible;
    }

    public String getEligibilityMessage() {
        return eligibilityMessage;
    }
}