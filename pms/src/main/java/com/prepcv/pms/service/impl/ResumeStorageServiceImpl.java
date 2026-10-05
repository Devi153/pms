package com.prepcv.pms.service.impl;

import com.prepcv.pms.exception.ResourceNotFoundException;
import com.prepcv.pms.service.ResumeStorageService;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.UUID;

@Service
public class ResumeStorageServiceImpl
        implements ResumeStorageService {

    private static final long MAX_FILE_SIZE =
            5L * 1024L * 1024L;

    private static final String PDF_CONTENT_TYPE =
            "application/pdf";

    private static final byte[] PDF_SIGNATURE = {
            0x25,
            0x50,
            0x44,
            0x46
    };

    private final Path resumeStorageLocation;

    public ResumeStorageServiceImpl(
            @Value("${app.resume.storage-path}")
            String storagePath) {

        try {

            this.resumeStorageLocation =
                    Paths
                            .get(storagePath)
                            .toAbsolutePath()
                            .normalize();

            Files.createDirectories(
                    this.resumeStorageLocation
            );

        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Could not initialize resume storage",
                    exception
            );
        }
    }

    // =========================================================
    // STORE RESUME
    // =========================================================

    @Override
    public String storeResume(
            MultipartFile file) {

        validateResume(file);

        String generatedFileName =
                UUID.randomUUID()
                        .toString()
                        + ".pdf";

        Path targetPath =
                resumeStorageLocation
                        .resolve(generatedFileName)
                        .normalize();

        // Security check
        if (!targetPath.startsWith(
                resumeStorageLocation)) {

            throw new IllegalStateException(
                    "Invalid resume storage path"
            );
        }

        try {

            Files.copy(
                    file.getInputStream(),
                    targetPath
            );

            /*
             * Store a logical path in the database.
             *
             * Do not store the absolute filesystem
             * path in the database.
             */

            return "/resumes/"
                    + generatedFileName;

        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Failed to store resume",
                    exception
            );
        }
    }

    // =========================================================
    // LOAD RESUME
    // =========================================================

    @Override
    public Resource loadResume(
            String resumeUrl) {

        if (resumeUrl == null ||
                resumeUrl.isBlank()) {

            throw new ResourceNotFoundException(
                    "Resume not found"
            );
        }

        String fileName =
                extractFileName(
                        resumeUrl
                );

        Path filePath =
                resumeStorageLocation
                        .resolve(fileName)
                        .normalize();

        // Security check
        if (!filePath.startsWith(
                resumeStorageLocation)) {

            throw new IllegalStateException(
                    "Invalid resume path"
            );
        }

        try {

            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );

            if (!resource.exists() ||
                    !resource.isReadable()) {

                throw new ResourceNotFoundException(
                        "Resume file not found"
                );
            }

            return resource;

        } catch (IOException exception) {

            throw new ResourceNotFoundException(
                    "Could not load resume"
            );
        }
    }

    // =========================================================
    // DELETE RESUME
    // =========================================================

    @Override
    public void deleteResume(
            String resumeUrl) {

        if (resumeUrl == null ||
                resumeUrl.isBlank()) {

            return;
        }

        String fileName =
                extractFileName(
                        resumeUrl
                );

        Path filePath =
                resumeStorageLocation
                        .resolve(fileName)
                        .normalize();

        if (!filePath.startsWith(
                resumeStorageLocation)) {

            throw new IllegalStateException(
                    "Invalid resume path"
            );
        }

        try {

            Files.deleteIfExists(
                    filePath
            );

        } catch (IOException exception) {

            /*
             * Do not break the profile update because
             * the old file could not be deleted.
             *
             * In production, this should be logged and
             * handled by a cleanup job.
             */

            System.err.println(
                    "Failed to delete old resume: "
                            + exception.getMessage()
            );
        }
    }

    // =========================================================
    // VALIDATE RESUME
    // =========================================================

    private void validateResume(
            MultipartFile file) {

        if (file == null ||
                file.isEmpty()) {

            throw new IllegalArgumentException(
                    "Resume file is required"
            );
        }

        // ---------------------------------------------
        // Size validation
        // ---------------------------------------------

        if (file.getSize() > MAX_FILE_SIZE) {

            throw new IllegalArgumentException(
                    "Resume file size must not exceed 5 MB"
            );
        }

        // ---------------------------------------------
        // Content type validation
        // ---------------------------------------------

        String contentType =
                file.getContentType();

        if (!PDF_CONTENT_TYPE.equalsIgnoreCase(
                contentType)) {

            throw new IllegalArgumentException(
                    "Only PDF resumes are allowed"
            );
        }

        // ---------------------------------------------
        // Filename validation
        // ---------------------------------------------

        String originalFilename =
                file.getOriginalFilename();

        if (originalFilename == null ||
                !originalFilename
                        .toLowerCase()
                        .endsWith(".pdf")) {

            throw new IllegalArgumentException(
                    "Resume must have a .pdf extension"
            );
        }

        // ---------------------------------------------
        // PDF magic-byte validation
        // ---------------------------------------------

        if (!isPdfSignature(file)) {

            throw new IllegalArgumentException(
                    "The uploaded file is not a valid PDF"
            );
        }
    }

    // =========================================================
    // CHECK PDF SIGNATURE
    // =========================================================

    private boolean isPdfSignature(
            MultipartFile file) {

        try (InputStream inputStream =
                     file.getInputStream()) {

            byte[] header =
                    new byte[4];

            int bytesRead =
                    inputStream.read(header);

            if (bytesRead != 4) {
                return false;
            }

            for (int i = 0; i < 4; i++) {

                if (header[i] !=
                        PDF_SIGNATURE[i]) {

                    return false;
                }
            }

            return true;

        } catch (IOException exception) {

            return false;
        }
    }

    // =========================================================
    // EXTRACT FILE NAME
    // =========================================================

    private String extractFileName(
            String resumeUrl) {

        String normalized =
                resumeUrl.replace("\\", "/");

        int lastSlash =
                normalized.lastIndexOf("/");

        String fileName =
                lastSlash >= 0
                        ? normalized.substring(
                        lastSlash + 1
                )
                        : normalized;

        if (fileName.isBlank()) {

            throw new IllegalStateException(
                    "Invalid resume filename"
            );
        }

        return fileName;
    }
}