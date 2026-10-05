package com.prepcv.pms.dto;

import com.prepcv.pms.entity.DriveStatus;

public class RecentPlacementDriveResponse {

    private Long driveId;
    private String companyName;
    private String jobTitle;
    private DriveStatus status;
    private long applicationCount;

    public RecentPlacementDriveResponse() {
    }

    public RecentPlacementDriveResponse(
            Long driveId,
            String companyName,
            String jobTitle,
            DriveStatus status,
            long applicationCount) {

        this.driveId = driveId;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.status = status;
        this.applicationCount = applicationCount;
    }

    public Long getDriveId() {
        return driveId;
    }

    public void setDriveId(Long driveId) {
        this.driveId = driveId;
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

    public DriveStatus getStatus() {
        return status;
    }

    public void setStatus(DriveStatus status) {
        this.status = status;
    }

    public long getApplicationCount() {
        return applicationCount;
    }

    public void setApplicationCount(long applicationCount) {
        this.applicationCount = applicationCount;
    }
}