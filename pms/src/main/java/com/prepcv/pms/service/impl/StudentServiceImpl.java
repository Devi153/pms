package com.prepcv.pms.service.impl;

import com.prepcv.pms.dto.StudentProfileRequest;
import com.prepcv.pms.dto.StudentProfileResponse;
import com.prepcv.pms.entity.Student;
import com.prepcv.pms.entity.User;
import com.prepcv.pms.exception.ResourceNotFoundException;
import com.prepcv.pms.repository.StudentRepository;
import com.prepcv.pms.repository.UserRepository;
import com.prepcv.pms.service.ResumeStorageService;
import com.prepcv.pms.service.StudentService;

import org.springframework.core.io.Resource;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class StudentServiceImpl
        implements StudentService {

    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    private final ResumeStorageService resumeStorageService;

    public StudentServiceImpl(
            StudentRepository studentRepository,
            UserRepository userRepository,
            ResumeStorageService resumeStorageService) {

        this.studentRepository =
                studentRepository;

        this.userRepository =
                userRepository;

        this.resumeStorageService =
                resumeStorageService;
    }

    // =========================================================
    // GET MY PROFILE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public StudentProfileResponse getMyProfile(
            Long userId) {

        Student student =
                studentRepository
                        .findByUserUserId(userId)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Student profile not found"
                                        )
                        );

        return mapToResponse(student);
    }

    // =========================================================
    // CREATE / UPDATE MY PROFILE
    // =========================================================

    @Override
    public StudentProfileResponse updateMyProfile(
            Long userId,
            StudentProfileRequest request,
            MultipartFile resume) {

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "User not found with id: "
                                                        + userId
                                        )
                        );

        Student student =
                studentRepository
                        .findByUserUserId(userId)
                        .orElseGet(() -> {

                            Student newStudent =
                                    new Student();

                            newStudent.setUser(user);

                            return newStudent;
                        });

        // =====================================================
        // UPDATE PROFILE FIELDS
        // =====================================================

        student.setFullName(
                request.getFullName().trim()
        );

        student.setPhoneNumber(
                trimToNull(
                        request.getPhoneNumber()
                )
        );

        student.setDepartment(
                trimToNull(
                        request.getDepartment()
                )
        );

        student.setGraduationYear(
                request.getGraduationYear()
        );

        student.setCgpa(
                request.getCgpa()
        );

        student.setTenthPercentage(
                request.getTenthPercentage()
        );

        student.setTwelfthPercentage(
                request.getTwelfthPercentage()
        );

        student.setSkills(
                trimToNull(
                        request.getSkills()
                )
        );

        // =====================================================
        // RESUME
        // =====================================================

        if (resume != null &&
                !resume.isEmpty()) {

            String oldResumeUrl =
                    student.getResumeUrl();

            String newResumeUrl =
                    resumeStorageService
                            .storeResume(
                                    resume
                            );

            student.setResumeUrl(
                    newResumeUrl
            );

            Student savedStudent =
                    studentRepository.save(student);

            // ---------------------------------------------
            // Delete old resume only after DB update
            // ---------------------------------------------

            if (oldResumeUrl != null &&
                    !oldResumeUrl.isBlank() &&
                    !oldResumeUrl.equals(
                            newResumeUrl
                    )) {

                resumeStorageService
                        .deleteResume(
                                oldResumeUrl
                        );
            }

            return mapToResponse(
                    savedStudent
            );
        }

        // =====================================================
        // NO NEW RESUME
        // =====================================================

        Student savedStudent =
                studentRepository.save(student);

        return mapToResponse(
                savedStudent
        );
    }

    // =========================================================
    // GET MY RESUME
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Resource getMyResume(
            Long userId) {

        Student student =
                studentRepository
                        .findByUserUserId(userId)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Student profile not found"
                                        )
                        );

        String resumeUrl =
                student.getResumeUrl();

        if (resumeUrl == null ||
                resumeUrl.isBlank()) {

            throw new ResourceNotFoundException(
                    "Resume not found"
            );
        }

        return resumeStorageService
                .loadResume(
                        resumeUrl
                );
    }

    // =========================================================
    // MAP ENTITY TO RESPONSE
    // =========================================================

    private StudentProfileResponse mapToResponse(
            Student student) {

        User user =
                student.getUser();

        String resumeUrl =
                student.getResumeUrl();

        /*
         * Store only the controlled API URL in the
         * response rather than exposing the physical
         * filesystem path.
         */

        if (resumeUrl != null &&
                !resumeUrl.isBlank()) {

            resumeUrl =
                    "/api/student/profile/resume";
        }

        return new StudentProfileResponse(
                student.getStudentId(),
                user.getUserId(),
                user.getEmail(),
                student.getFullName(),
                student.getPhoneNumber(),
                student.getDepartment(),
                student.getGraduationYear(),
                student.getCgpa(),
                student.getTenthPercentage(),
                student.getTwelfthPercentage(),
                student.getSkills(),
                resumeUrl
        );
    }

    // =========================================================
    // STRING CLEANUP
    // =========================================================

    private String trimToNull(
            String value) {

        if (value == null) {
            return null;
        }

        String trimmed =
                value.trim();

        return trimmed.isEmpty()
                ? null
                : trimmed;
    }
}