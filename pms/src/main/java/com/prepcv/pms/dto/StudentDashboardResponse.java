package com.prepcv.pms.dto;

public class StudentDashboardResponse {

    private long totalApplications;
    private long upcomingInterviews;
    private long shortlistedApplications;
    private int profileCompletion;

    public StudentDashboardResponse() {
    }

    public StudentDashboardResponse(
            long totalApplications,
            long upcomingInterviews,
            long shortlistedApplications,
            int profileCompletion) {

        this.totalApplications = totalApplications;
        this.upcomingInterviews = upcomingInterviews;
        this.shortlistedApplications = shortlistedApplications;
        this.profileCompletion = profileCompletion;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getUpcomingInterviews() {
        return upcomingInterviews;
    }

    public void setUpcomingInterviews(long upcomingInterviews) {
        this.upcomingInterviews = upcomingInterviews;
    }

    public long getShortlistedApplications() {
        return shortlistedApplications;
    }

    public void setShortlistedApplications(
            long shortlistedApplications) {

        this.shortlistedApplications =
                shortlistedApplications;
    }

    public int getProfileCompletion() {
        return profileCompletion;
    }

    public void setProfileCompletion(int profileCompletion) {
        this.profileCompletion = profileCompletion;
    }
}