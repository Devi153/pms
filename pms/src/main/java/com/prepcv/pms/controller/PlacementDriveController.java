package com.prepcv.pms.controller;

import com.prepcv.pms.dto.PlacementDriveRequest;
import com.prepcv.pms.dto.PlacementDriveResponse;
import com.prepcv.pms.service.PlacementDriveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.prepcv.pms.dto.PlacementDriveStatusRequest;

import java.util.List;

@RestController
@RequestMapping("/api/admin/drives")
public class PlacementDriveController {

    private final PlacementDriveService placementDriveService;

    public PlacementDriveController(
            PlacementDriveService placementDriveService) {

        this.placementDriveService = placementDriveService;
    }

    // Create a new placement drive
    @PostMapping
    public ResponseEntity<PlacementDriveResponse> createDrive(
            @Valid @RequestBody PlacementDriveRequest request) {

        PlacementDriveResponse response =
                placementDriveService.createDrive(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all placement drives
    @GetMapping
    public ResponseEntity<List<PlacementDriveResponse>> getAllDrives() {

        return ResponseEntity.ok(
                placementDriveService.getAllDrives()
        );
    }

    // Get one placement drive
    @GetMapping("/{driveId}")
    public ResponseEntity<PlacementDriveResponse> getDriveById(
            @PathVariable Long driveId) {

        return ResponseEntity.ok(
                placementDriveService.getDriveById(driveId)
        );
    }

    // Update a placement drive
    @PutMapping("/{driveId}")
    public ResponseEntity<PlacementDriveResponse> updateDrive(
            @PathVariable Long driveId,
            @Valid @RequestBody PlacementDriveRequest request) {

        return ResponseEntity.ok(
                placementDriveService.updateDrive(
                        driveId,
                        request
                )
        );
    }

    // Update placement drive status
    @PatchMapping("/{driveId}/status")
    public ResponseEntity<PlacementDriveResponse> updateDriveStatus(
            @PathVariable Long driveId,
            @Valid @RequestBody PlacementDriveStatusRequest request) {

        return ResponseEntity.ok(
                placementDriveService.updateDriveStatus(
                        driveId,
                        request.getStatus()
                )
        );
    }

    // Delete a placement drive
    @DeleteMapping("/{driveId}")
    public ResponseEntity<Void> deleteDrive(
            @PathVariable Long driveId) {

        placementDriveService.deleteDrive(driveId);

        return ResponseEntity.noContent().build();
    }
}