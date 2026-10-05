package com.prepcv.pms.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "placement_drives")
public class PlacementDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drive_id")
    private Long driveId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "company_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_drive_company")
    )
    private Company company;

    @Column(name = "job_title", nullable = false, length = 150)
    private String jobTitle;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "minimum_cgpa", precision = 4, scale = 2)
    private BigDecimal minimumCgpa;

    @Column(name = "minimum_tenth_percentage", precision = 5, scale = 2)
    private BigDecimal minimumTenthPercentage;

    @Column(name = "minimum_twelfth_percentage", precision = 5, scale = 2)
    private BigDecimal minimumTwelfthPercentage;

    @Column(name = "application_deadline", nullable = false)
    private LocalDate applicationDeadline;

    @Column(name = "drive_date")
    private LocalDate driveDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DriveStatus status = DriveStatus.DRAFT;

    public PlacementDrive() {
    }

    public Long getDriveId() {
        return driveId;
    }

    public void setDriveId(Long driveId) {
        this.driveId = driveId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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