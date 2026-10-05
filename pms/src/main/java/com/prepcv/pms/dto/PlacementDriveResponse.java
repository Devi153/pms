package com.prepcv.pms.dto;

import com.prepcv.pms.entity.DriveStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PlacementDriveResponse {

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

    public PlacementDriveResponse() {
    }

    public PlacementDriveResponse(
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
            DriveStatus status) {

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
    }

    public Long getDriveId() {
        return driveId;
    }

    public void setDriveId(Long driveId) {
        this.driveId = driveId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMinimumCgpa() {
        return minimumCgpa;
    }

    public void setMinimumCgpa(BigDecimal minimumCgpa) {
        this.minimumCgpa = minimumCgpa;
    }

    public BigDecimal getMinimumTenthPercentage() {
        return minimumTenthPercentage;
    }

    public void setMinimumTenthPercentage(
            BigDecimal minimumTenthPercentage) {

        this.minimumTenthPercentage = minimumTenthPercentage;
    }

    public BigDecimal getMinimumTwelfthPercentage() {
        return minimumTwelfthPercentage;
    }

    public void setMinimumTwelfthPercentage(
            BigDecimal minimumTwelfthPercentage) {

        this.minimumTwelfthPercentage = minimumTwelfthPercentage;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(
            LocalDate applicationDeadline) {

        this.applicationDeadline = applicationDeadline;
    }

    public LocalDate getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(LocalDate driveDate) {
        this.driveDate = driveDate;
    }

    public DriveStatus getStatus() {
        return status;
    }

    public void setStatus(DriveStatus status) {
        this.status = status;
    }
}