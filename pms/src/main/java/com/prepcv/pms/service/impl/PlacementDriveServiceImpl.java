package com.prepcv.pms.service.impl;

import com.prepcv.pms.dto.PlacementDriveRequest;
import com.prepcv.pms.dto.PlacementDriveResponse;
import com.prepcv.pms.entity.Company;
import com.prepcv.pms.entity.DriveStatus;
import com.prepcv.pms.entity.PlacementDrive;
import com.prepcv.pms.exception.ResourceNotFoundException;
import com.prepcv.pms.repository.CompanyRepository;
import com.prepcv.pms.repository.PlacementDriveRepository;
import com.prepcv.pms.service.PlacementDriveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PlacementDriveServiceImpl
        implements PlacementDriveService {

    private final PlacementDriveRepository placementDriveRepository;
    private final CompanyRepository companyRepository;

    public PlacementDriveServiceImpl(
            PlacementDriveRepository placementDriveRepository,
            CompanyRepository companyRepository) {

        this.placementDriveRepository = placementDriveRepository;
        this.companyRepository = companyRepository;
    }

    // ---------------------------------------------------------
    // CREATE DRIVE
    // ---------------------------------------------------------

    @Override
    public PlacementDriveResponse createDrive(
            PlacementDriveRequest request) {

        validateDates(request);

        Company company = companyRepository
                .findById(request.getCompanyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with id: "
                                        + request.getCompanyId()
                        ));

        PlacementDrive drive = new PlacementDrive();

        drive.setCompany(company);

        drive.setJobTitle(
                request.getJobTitle().trim()
        );

        drive.setDescription(
                request.getDescription()
        );

        drive.setMinimumCgpa(
                request.getMinimumCgpa()
        );

        drive.setMinimumTenthPercentage(
                request.getMinimumTenthPercentage()
        );

        drive.setMinimumTwelfthPercentage(
                request.getMinimumTwelfthPercentage()
        );

        drive.setApplicationDeadline(
                request.getApplicationDeadline()
        );

        drive.setDriveDate(
                request.getDriveDate()
        );

        // Every newly created drive starts as DRAFT.
        drive.setStatus(DriveStatus.DRAFT);

        PlacementDrive savedDrive =
                placementDriveRepository.save(drive);

        return mapToResponse(savedDrive);
    }

    // ---------------------------------------------------------
    // ADMIN - GET ALL DRIVES
    // ---------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<PlacementDriveResponse> getAllDrives() {

        return placementDriveRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ---------------------------------------------------------
    // STUDENT - GET OPEN DRIVES
    // ---------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<PlacementDriveResponse>
    getOpenDrivesForStudents() {

        LocalDate today = LocalDate.now();

        return placementDriveRepository
                .findByStatusAndApplicationDeadlineGreaterThanEqual(
                        DriveStatus.OPEN,
                        today
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ---------------------------------------------------------
    // GET ONE DRIVE
    // ---------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public PlacementDriveResponse getDriveById(
            Long driveId) {

        PlacementDrive drive =
                placementDriveRepository.findById(driveId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Placement drive not found with id: "
                                                + driveId
                                ));

        return mapToResponse(drive);
    }

    // ---------------------------------------------------------
    // UPDATE DRIVE
    // ---------------------------------------------------------

    @Override
    public PlacementDriveResponse updateDrive(
            Long driveId,
            PlacementDriveRequest request) {

        validateDates(request);

        PlacementDrive drive =
                placementDriveRepository.findById(driveId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Placement drive not found with id: "
                                                + driveId
                                ));

        Company company =
                companyRepository.findById(
                        request.getCompanyId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with id: "
                                        + request.getCompanyId()
                        ));

        drive.setCompany(company);

        drive.setJobTitle(
                request.getJobTitle().trim()
        );

        drive.setDescription(
                request.getDescription()
        );

        drive.setMinimumCgpa(
                request.getMinimumCgpa()
        );

        drive.setMinimumTenthPercentage(
                request.getMinimumTenthPercentage()
        );

        drive.setMinimumTwelfthPercentage(
                request.getMinimumTwelfthPercentage()
        );

        drive.setApplicationDeadline(
                request.getApplicationDeadline()
        );

        drive.setDriveDate(
                request.getDriveDate()
        );

        /*
         * The existing status is intentionally preserved.
         *
         * Example:
         *
         * OPEN → update job details → remains OPEN
         *
         * Status changes must happen through the
         * dedicated status endpoint.
         */

        PlacementDrive updatedDrive =
                placementDriveRepository.save(drive);

        return mapToResponse(updatedDrive);
    }

    // ---------------------------------------------------------
    // UPDATE STATUS
    // ---------------------------------------------------------

    @Override
    public PlacementDriveResponse updateDriveStatus(
            Long driveId,
            DriveStatus newStatus) {

        PlacementDrive drive =
                placementDriveRepository.findById(driveId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Placement drive not found with id: "
                                                + driveId
                                ));

        DriveStatus currentStatus =
                drive.getStatus();

        validateStatusTransition(
                currentStatus,
                newStatus
        );

        drive.setStatus(newStatus);

        PlacementDrive updatedDrive =
                placementDriveRepository.save(drive);

        return mapToResponse(updatedDrive);
    }

    // ---------------------------------------------------------
    // DELETE DRIVE
    // ---------------------------------------------------------

    @Override
    public void deleteDrive(Long driveId) {

        PlacementDrive drive =
                placementDriveRepository.findById(driveId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Placement drive not found with id: "
                                                + driveId
                                ));

        DriveStatus status = drive.getStatus();

        /*
         * A drive can only be deleted while it is:
         *
         * DRAFT
         * or
         * CANCELLED
         *
         * OPEN and CLOSED drives are retained because
         * they represent actual placement activity/history.
         */

        if (status != DriveStatus.DRAFT
                && status != DriveStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Only DRAFT or CANCELLED placement drives "
                            + "can be deleted"
            );
        }

        placementDriveRepository.delete(drive);
    }

    // ---------------------------------------------------------
    // DATE VALIDATION
    // ---------------------------------------------------------

    private void validateDates(
            PlacementDriveRequest request) {

        LocalDate today = LocalDate.now();

        if (request.getApplicationDeadline()
                .isBefore(today)) {

            throw new IllegalArgumentException(
                    "Application deadline cannot be in the past"
            );
        }

        if (request.getDriveDate() != null
                && request.getDriveDate()
                .isBefore(request.getApplicationDeadline())) {

            throw new IllegalArgumentException(
                    "Drive date cannot be before application deadline"
            );
        }
    }

    // ---------------------------------------------------------
    // STATUS TRANSITION VALIDATION
    // ---------------------------------------------------------

    private void validateStatusTransition(
            DriveStatus currentStatus,
            DriveStatus newStatus) {

        if (currentStatus == newStatus) {
            return;
        }

        boolean validTransition =
                switch (currentStatus) {

                    case DRAFT ->
                            newStatus == DriveStatus.OPEN
                                    || newStatus == DriveStatus.CANCELLED;

                    case OPEN ->
                            newStatus == DriveStatus.CLOSED
                                    || newStatus == DriveStatus.CANCELLED;

                    case CLOSED,
                         CANCELLED ->
                            false;
                };

        if (!validTransition) {

            throw new IllegalStateException(
                    "Invalid placement drive status transition: "
                            + currentStatus
                            + " -> "
                            + newStatus
            );
        }
    }

    // ---------------------------------------------------------
    // ENTITY → RESPONSE DTO
    // ---------------------------------------------------------

    private PlacementDriveResponse mapToResponse(
            PlacementDrive drive) {

        return new PlacementDriveResponse(
                drive.getDriveId(),
                drive.getCompany().getCompanyId(),
                drive.getCompany().getCompanyName(),
                drive.getJobTitle(),
                drive.getDescription(),
                drive.getMinimumCgpa(),
                drive.getMinimumTenthPercentage(),
                drive.getMinimumTwelfthPercentage(),
                drive.getApplicationDeadline(),
                drive.getDriveDate(),
                drive.getStatus()
        );
    }
}