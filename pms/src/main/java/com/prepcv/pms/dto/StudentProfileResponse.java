package com.prepcv.pms.dto;

import java.math.BigDecimal;

public class StudentProfileResponse {

    private Long studentId;

    private Long userId;

    private String email;

    private String fullName;

    private String phoneNumber;

    private String department;

    private Integer graduationYear;

    private BigDecimal cgpa;

    private BigDecimal tenthPercentage;

    private BigDecimal twelfthPercentage;

    private String skills;

    private String resumeUrl;

    public StudentProfileResponse() {
    }

    public StudentProfileResponse(
            Long studentId,
            Long userId,
            String email,
            String fullName,
            String phoneNumber,
            String department,
            Integer graduationYear,
            BigDecimal cgpa,
            BigDecimal tenthPercentage,
            BigDecimal twelfthPercentage,
            String skills,
            String resumeUrl) {

        this.studentId = studentId;
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.graduationYear = graduationYear;
        this.cgpa = cgpa;
        this.tenthPercentage = tenthPercentage;
        this.twelfthPercentage = twelfthPercentage;
        this.skills = skills;
        this.resumeUrl = resumeUrl;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(
            Integer graduationYear) {

        this.graduationYear =
                graduationYear;
    }

    public BigDecimal getCgpa() {
        return cgpa;
    }

    public void setCgpa(BigDecimal cgpa) {
        this.cgpa = cgpa;
    }

    public BigDecimal getTenthPercentage() {
        return tenthPercentage;
    }

    public void setTenthPercentage(
            BigDecimal tenthPercentage) {

        this.tenthPercentage =
                tenthPercentage;
    }

    public BigDecimal getTwelfthPercentage() {
        return twelfthPercentage;
    }

    public void setTwelfthPercentage(
            BigDecimal twelfthPercentage) {

        this.twelfthPercentage =
                twelfthPercentage;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }
}