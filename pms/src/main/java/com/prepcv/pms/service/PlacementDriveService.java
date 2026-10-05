package com.prepcv.pms.service;

import com.prepcv.pms.dto.PlacementDriveRequest;
import com.prepcv.pms.dto.PlacementDriveResponse;
import com.prepcv.pms.entity.DriveStatus;

import java.util.List;

public interface PlacementDriveService {

    PlacementDriveResponse createDrive(
            PlacementDriveRequest request
    );

    List<PlacementDriveResponse> getAllDrives();

    List<PlacementDriveResponse> getOpenDrivesForStudents();

    PlacementDriveResponse getDriveById(
            Long driveId
    );

    PlacementDriveResponse updateDrive(
            Long driveId,
            PlacementDriveRequest request
    );

    PlacementDriveResponse updateDriveStatus(
            Long driveId,
            DriveStatus status
    );

    void deleteDrive(Long driveId);
}