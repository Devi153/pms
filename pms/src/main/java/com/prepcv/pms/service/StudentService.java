package com.prepcv.pms.service;

import com.prepcv.pms.dto.StudentProfileRequest;
import com.prepcv.pms.dto.StudentProfileResponse;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {

    StudentProfileResponse getMyProfile(
            Long userId
    );

    StudentProfileResponse updateMyProfile(
            Long userId,
            StudentProfileRequest request,
            MultipartFile resume
    );

    Resource getMyResume(
            Long userId
    );
}