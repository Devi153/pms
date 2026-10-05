package com.prepcv.pms.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PlacementDriveRequest {

    @NotNull(message = "Company ID is required")
    @Positive(message = "Company ID must be positive")
    private Long companyId;

    @NotBlank(message = "Job title is required")
    @Size(
            max = 150,
            message = "Job title must not exceed 150 characters"
    )
    private String jobTitle;

    @Size(
            max = 5000,
            message = "Description must not exceed 5000 characters"
    )
    private String description;

    @DecimalMin(
            value = "0.00",
            message = "Minimum CGPA cannot be less than 0"
    )
    @DecimalMax(
            value = "10.00",
            message = "Minimum CGPA cannot be greater than 10"
    )
    @Digits(
            integer = 2,
            fraction = 2,
            message = "Invalid CGPA format"
    )
    private BigDecimal minimumCgpa;

    @DecimalMin(
            value = "0.00",
            message = "Minimum 10th percentage cannot be less than 0"
    )
    @DecimalMax(
            value = "100.00",
            message = "Minimum 10th percentage cannot be greater than 100"
    )
    @Digits(
            integer = 3,
            fraction = 2,
            message = "Invalid 10th percentage format"
    )
    private BigDecimal minimumTenthPercentage;

    @DecimalMin(
            value = "0.00",
            message = "Minimum 12th percentage cannot be less than 0"
    )
    @DecimalMax(
            value = "100.00",
            message = "Minimum 12th percentage cannot be greater than 100"
    )
    @Digits(
            integer = 3,
            fraction = 2,
            message = "Invalid 12th percentage format"
    )
    private BigDecimal minimumTwelfthPercentage;

    @NotNull(message = "Application deadline is required")
    private LocalDate applicationDeadline;

    private LocalDate driveDate;

    public PlacementDriveRequest() {
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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
}