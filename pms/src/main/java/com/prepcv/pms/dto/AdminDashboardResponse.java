package com.prepcv.pms.dto;

import java.util.List;

public class AdminDashboardResponse {

    private long totalStudents;
    private long totalCompanies;
    private long totalPlacementDrives;
    private long totalApplications;
    private List<RecentPlacementDriveResponse> recentDrives;

    public AdminDashboardResponse() {
    }

    public AdminDashboardResponse(
            long totalStudents,
            long totalCompanies,
            long totalPlacementDrives,
            long totalApplications,
            List<RecentPlacementDriveResponse> recentDrives) {

        this.totalStudents = totalStudents;
        this.totalCompanies = totalCompanies;
        this.totalPlacementDrives = totalPlacementDrives;
        this.totalApplications = totalApplications;
        this.recentDrives = recentDrives;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalCompanies() {
        return totalCompanies;
    }

    public void setTotalCompanies(long totalCompanies) {
        this.totalCompanies = totalCompanies;
    }

    public long getTotalPlacementDrives() {
        return totalPlacementDrives;
    }

    public void setTotalPlacementDrives(long totalPlacementDrives) {
        this.totalPlacementDrives = totalPlacementDrives;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public List<RecentPlacementDriveResponse> getRecentDrives() {
        return recentDrives;
    }

    public void setRecentDrives(
            List<RecentPlacementDriveResponse> recentDrives) {

        this.recentDrives = recentDrives;
    }
}