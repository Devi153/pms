package com.prepcv.pms.repository;

import com.prepcv.pms.entity.DriveStatus;
import com.prepcv.pms.entity.PlacementDrive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PlacementDriveRepository
        extends JpaRepository<PlacementDrive, Long> {

    List<PlacementDrive> findByStatus(
            DriveStatus status
    );

    List<PlacementDrive>
    findByStatusAndApplicationDeadlineGreaterThanEqual(
            DriveStatus status,
            LocalDate date
    );

    List<PlacementDrive>
    findTop5ByOrderByDriveIdDesc();
}