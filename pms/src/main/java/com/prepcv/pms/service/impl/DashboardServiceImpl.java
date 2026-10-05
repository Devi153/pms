package com.prepcv.pms.service.impl;

import com.prepcv.pms.dto.AdminDashboardResponse;
import com.prepcv.pms.dto.RecentPlacementDriveResponse;
import com.prepcv.pms.dto.StudentDashboardResponse;
import com.prepcv.pms.entity.Role;
import com.prepcv.pms.entity.Student;
import com.prepcv.pms.entity.User;
import com.prepcv.pms.repository.CompanyRepository;
import com.prepcv.pms.repository.PlacementDriveRepository;
import com.prepcv.pms.repository.StudentRepository;
import com.prepcv.pms.repository.UserRepository;
import com.prepcv.pms.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl
        implements DashboardService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;
    private final PlacementDriveRepository placementDriveRepository;

    public DashboardServiceImpl(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            StudentRepository studentRepository,
            PlacementDriveRepository placementDriveRepository) {

        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.studentRepository = studentRepository;
        this.placementDriveRepository = placementDriveRepository;
    }

    @Override
    public AdminDashboardResponse getAdminDashboard() {

        long totalStudents =
                userRepository.countByRole(Role.STUDENT);

        long totalCompanies =
                companyRepository.count();

        long totalPlacementDrives =
                placementDriveRepository.count();

        /*
         * Application module is not implemented yet.
         * Therefore we deliberately return 0 instead of
         * displaying fake application data.
         */
        long totalApplications = 0;

        List<RecentPlacementDriveResponse> recentDrives =
                placementDriveRepository
                        .findTop5ByOrderByDriveIdDesc()
                        .stream()
                        .map(drive ->
                                new RecentPlacementDriveResponse(
                                        drive.getDriveId(),
                                        drive.getCompany()
                                                .getCompanyName(),
                                        drive.getJobTitle(),
                                        drive.getStatus(),
                                        0
                                )
                        )
                        .toList();

        return new AdminDashboardResponse(
                totalStudents,
                totalCompanies,
                totalPlacementDrives,
                totalApplications,
                recentDrives
        );
    }

    @Override
    public StudentDashboardResponse getStudentDashboard(
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Authenticated user was not found."
                        )
                );

        Student student = studentRepository
                .findByUserUserId(user.getUserId())
                .orElse(null);

        /*
         * Application functionality is not implemented yet.
         * Keep these values at the real empty state.
         */
        long totalApplications = 0;
        long upcomingInterviews = 0;
        long shortlistedApplications = 0;

        int profileCompletion =
                calculateProfileCompletion(
                        user,
                        student
                );

        return new StudentDashboardResponse(
                totalApplications,
                upcomingInterviews,
                shortlistedApplications,
                profileCompletion
        );
    }

    private int calculateProfileCompletion(
            User user,
            Student student) {

        if (student == null) {
            return 0;
        }

        int totalFields = 10;
        int completedFields = 0;

        /*
         * User information
         */
        if (user.getEmail() != null
                && !user.getEmail().isBlank()) {

            completedFields++;
        }

        /*
         * Student information
         */
        if (student.getFullName() != null
                && !student.getFullName().isBlank()) {

            completedFields++;
        }

        if (student.getPhoneNumber() != null
                && !student.getPhoneNumber().isBlank()) {

            completedFields++;
        }

        if (student.getDepartment() != null
                && !student.getDepartment().isBlank()) {

            completedFields++;
        }

        if (student.getGraduationYear() != null) {
            completedFields++;
        }

        if (student.getCgpa() != null) {
            completedFields++;
        }

        if (student.getTenthPercentage() != null) {
            completedFields++;
        }

        if (student.getTwelfthPercentage() != null) {
            completedFields++;
        }

        if (student.getSkills() != null
                && !student.getSkills().isBlank()) {

            completedFields++;
        }

        if (student.getResumeUrl() != null
                && !student.getResumeUrl().isBlank()) {

            completedFields++;
        }

        return (completedFields * 100) / totalFields;
    }
}