package com.prepcv.pms.controller;

import com.prepcv.pms.dto.PlacementDriveResponse;
import com.prepcv.pms.service.PlacementDriveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student/drives")
public class StudentPlacementDriveController {

    private final PlacementDriveService placementDriveService;

    public StudentPlacementDriveController(
            PlacementDriveService placementDriveService) {

        this.placementDriveService = placementDriveService;
    }

    /**
     * Returns placement drives currently available
     * for students.
     *
     * Only OPEN drives whose application deadline
     * has not passed are returned.
     */
    @GetMapping
    public ResponseEntity<List<PlacementDriveResponse>>
    getAvailableDrives() {

        return ResponseEntity.ok(
                placementDriveService
                        .getOpenDrivesForStudents()
        );
    }
}