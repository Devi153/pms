package com.prepcv.pms.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class StudentProfileRequest {

    @NotBlank(
            message = "Full name is required"
    )
    @Size(
            max = 150,
            message = "Full name must not exceed 150 characters"
    )
    private String fullName;

    @Pattern(
            regexp = "^[0-9+()\\- ]{10,20}$",
            message = "Enter a valid phone number"
    )
    private String phoneNumber;

    @Size(
            max = 100,
            message = "Department must not exceed 100 characters"
    )
    private String department;

    @Min(
            value = 2000,
            message = "Invalid graduation year"
    )
    @Max(
            value = 2100,
            message = "Invalid graduation year"
    )
    private Integer graduationYear;

    @DecimalMin(
            value = "0.00",
            message = "CGPA cannot be less than 0"
    )
    @DecimalMax(
            value = "10.00",
            message = "CGPA cannot be greater than 10"
    )
    @Digits(
            integer = 2,
            fraction = 2,
            message = "Invalid CGPA format"
    )
    private BigDecimal cgpa;

    @DecimalMin(
            value = "0.00",
            message = "10th percentage cannot be less than 0"
    )
    @DecimalMax(
            value = "100.00",
            message = "10th percentage cannot be greater than 100"
    )
    @Digits(
            integer = 3,
            fraction = 2,
            message = "Invalid 10th percentage format"
    )
    private BigDecimal tenthPercentage;

    @DecimalMin(
            value = "0.00",
            message = "12th percentage cannot be less than 0"
    )
    @DecimalMax(
            value = "100.00",
            message = "12th percentage cannot be greater than 100"
    )
    @Digits(
            integer = 3,
            fraction = 2,
            message = "Invalid 12th percentage format"
    )
    private BigDecimal twelfthPercentage;

    @Size(
            max = 5000,
            message = "Skills must not exceed 5000 characters"
    )
    private String skills;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public StudentProfileRequest() {
    }

    // =========================================================
    // GETTERS / SETTERS
    // =========================================================

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

        this.graduationYear = graduationYear;
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
}