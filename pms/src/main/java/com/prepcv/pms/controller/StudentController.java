package com.prepcv.pms.controller;

import com.prepcv.pms.dto.StudentProfileRequest;
import com.prepcv.pms.dto.StudentProfileResponse;
import com.prepcv.pms.entity.User;
import com.prepcv.pms.exception.ResourceNotFoundException;
import com.prepcv.pms.repository.UserRepository;
import com.prepcv.pms.service.StudentService;

import jakarta.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    private final UserRepository userRepository;

    public StudentController(
            StudentService studentService,
            UserRepository userRepository) {

        this.studentService = studentService;
        this.userRepository = userRepository;
    }

    // =========================================================
    // GET MY PROFILE
    // =========================================================

    @GetMapping("/profile")
    public ResponseEntity<StudentProfileResponse> getMyProfile(
            Authentication authentication) {

        String email = authentication.getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Authenticated user not found"
                                )
                        );

        Long userId = user.getUserId();

        return ResponseEntity.ok(
                studentService.getMyProfile(userId)
        );
    }

    // =========================================================
    // CREATE / UPDATE MY PROFILE
    // =========================================================

    @PutMapping(
            value = "/profile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<StudentProfileResponse> updateMyProfile(

            Authentication authentication,

            @Valid
            @RequestPart("profile")
            StudentProfileRequest request,

            @RequestPart(
                    value = "resume",
                    required = false
            )
            MultipartFile resume) {

        String email = authentication.getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Authenticated user not found"
                                )
                        );

        Long userId = user.getUserId();

        return ResponseEntity.ok(
                studentService.updateMyProfile(
                        userId,
                        request,
                        resume
                )
        );
    }

    // =========================================================
    // VIEW MY RESUME
    // =========================================================

    @GetMapping("/profile/resume")
    public ResponseEntity<Resource> viewMyResume(
            Authentication authentication) {

        String email = authentication.getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Authenticated user not found"
                                )
                        );

        Long userId = user.getUserId();

        Resource resource =
                studentService.getMyResume(userId);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_PDF
        );

        headers.setContentDisposition(
                ContentDisposition
                        .inline()
                        .filename("resume.pdf")
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}