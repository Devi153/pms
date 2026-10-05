package com.prepcv.pms.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "students",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_user",
                        columnNames = "user_id"
                )
        }
)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_student_user"
            )
    )
    private User user;

    @Column(
            name = "full_name",
            nullable = false,
            length = 150
    )
    private String fullName;

    @Column(
            name = "phone_number",
            length = 20
    )
    private String phoneNumber;

    @Column(
            name = "department",
            length = 100
    )
    private String department;

    @Column(
            name = "graduation_year"
    )
    private Integer graduationYear;

    @Column(
            name = "cgpa",
            precision = 4,
            scale = 2
    )
    private BigDecimal cgpa;

    @Column(
            name = "tenth_percentage",
            precision = 5,
            scale = 2
    )
    private BigDecimal tenthPercentage;

    @Column(
            name = "twelfth_percentage",
            precision = 5,
            scale = 2
    )
    private BigDecimal twelfthPercentage;

    @Column(
            name = "skills",
            columnDefinition = "TEXT"
    )
    private String skills;

    @Column(
            name = "resume_url",
            length = 500
    )
    private String resumeUrl;

    public Student() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public void setGraduationYear(Integer graduationYear) {
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

    public void setTenthPercentage(BigDecimal tenthPercentage) {
        this.tenthPercentage = tenthPercentage;
    }

    public BigDecimal getTwelfthPercentage() {
        return twelfthPercentage;
    }

    public void setTwelfthPercentage(
            BigDecimal twelfthPercentage) {
        this.twelfthPercentage = twelfthPercentage;
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