package com.prepcv.pms.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeStorageService {

    String storeResume(
            MultipartFile file
    );

    Resource loadResume(
            String resumeUrl
    );

    void deleteResume(
            String resumeUrl
    );
}