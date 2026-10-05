package com.prepcv.pms.service;

import com.prepcv.pms.dto.StudentPlacementDriveResponse;

import java.util.List;

public interface StudentPlacementDriveService {

    List<StudentPlacementDriveResponse> getAvailableDrives(Long userId);

    StudentPlacementDriveResponse getDriveById(
            Long userId,
            Long driveId
    );
}