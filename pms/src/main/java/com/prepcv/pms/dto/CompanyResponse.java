package com.prepcv.pms.dto;

import java.time.LocalDateTime;

public class CompanyResponse {

    private Long companyId;
    private String companyName;
    private String website;
    private String location;
    private LocalDateTime createdAt;

    public CompanyResponse(
            Long companyId,
            String companyName,
            String website,
            String location,
            LocalDateTime createdAt) {

        this.companyId = companyId;
        this.companyName = companyName;
        this.website = website;
        this.location = location;
        this.createdAt = createdAt;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getWebsite() {
        return website;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}