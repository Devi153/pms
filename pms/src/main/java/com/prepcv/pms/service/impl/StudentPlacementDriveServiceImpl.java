package com.prepcv.pms.service.impl;

import com.prepcv.pms.dto.StudentPlacementDriveResponse;
import com.prepcv.pms.entity.DriveStatus;
import com.prepcv.pms.entity.PlacementDrive;
import com.prepcv.pms.entity.Student;
import com.prepcv.pms.exception.ResourceNotFoundException;
import com.prepcv.pms.repository.PlacementDriveRepository;
import com.prepcv.pms.repository.StudentRepository;
import com.prepcv.pms.service.StudentPlacementDriveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentPlacementDriveServiceImpl
        implements StudentPlacementDriveService {

    private final StudentRepository studentRepository;
    private final PlacementDriveRepository placementDriveRepository;

    public StudentPlacementDriveServiceImpl(
            StudentRepository studentRepository,
            PlacementDriveRepository placementDriveRepository) {

        this.studentRepository = studentRepository;
        this.placementDriveRepository = placementDriveRepository;
    }

    @Override
    public List<StudentPlacementDriveResponse> getAvailableDrives(
            Long userId) {

        Student student = getStudentByUserId(userId);

        return placementDriveRepository
                .findByStatus(DriveStatus.OPEN)
                .stream()
                .map(drive -> mapToResponse(drive, student))
                .toList();
    }

    @Override
    public StudentPlacementDriveResponse getDriveById(
            Long userId,
            Long driveId) {

        Student student = getStudentByUserId(userId);

        PlacementDrive drive =
                placementDriveRepository.findById(driveId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Placement drive not found with id: "
                                                + driveId
                                )
                        );

        if (drive.getStatus() != DriveStatus.OPEN) {
            throw new ResourceNotFoundException(
                    "Placement drive is not currently available"
            );
        }

        return mapToResponse(drive, student);
    }

    private Student getStudentByUserId(Long userId) {

        return studentRepository
                .findByUserUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student profile not found"
                        )
                );
    }

    private StudentPlacementDriveResponse mapToResponse(
            PlacementDrive drive,
            Student student) {

        EligibilityResult eligibility =
                checkEligibility(drive, student);

        return new StudentPlacementDriveResponse(
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
                drive.getStatus(),
                eligibility.eligible(),
                eligibility.message()
        );
    }

    private EligibilityResult checkEligibility(
            PlacementDrive drive,
            Student student) {

        /*
         * Student academic information is required
         * for eligibility calculation.
         */

        if (student.getCgpa() == null) {

            return new EligibilityResult(
                    false,
                    "Update your CGPA in your profile"
            );
        }

        if (student.getTenthPercentage() == null) {

            return new EligibilityResult(
                    false,
                    "Update your 10th percentage in your profile"
            );
        }

        if (student.getTwelfthPercentage() == null) {

            return new EligibilityResult(
                    false,
                    "Update your 12th percentage in your profile"
            );
        }

        /*
         * Check CGPA requirement.
         */

        if (drive.getMinimumCgpa() != null
                && student.getCgpa()
                .compareTo(drive.getMinimumCgpa()) < 0) {

            return new EligibilityResult(
                    false,
                    "CGPA requirement not satisfied"
            );
        }

        /*
         * Check 10th percentage requirement.
         */

        if (drive.getMinimumTenthPercentage() != null
                && student.getTenthPercentage()
                .compareTo(
                        drive.getMinimumTenthPercentage()
                ) < 0) {

            return new EligibilityResult(
                    false,
                    "10th percentage requirement not satisfied"
            );
        }

        /*
         * Check 12th percentage requirement.
         */

        if (drive.getMinimumTwelfthPercentage() != null
                && student.getTwelfthPercentage()
                .compareTo(
                        drive.getMinimumTwelfthPercentage()
                ) < 0) {

            return new EligibilityResult(
                    false,
                    "12th percentage requirement not satisfied"
            );
        }

        /*
         * All requirements satisfied.
         */

        return new EligibilityResult(
                true,
                "You meet the eligibility requirements"
        );
    }

    private record EligibilityResult(
            boolean eligible,
            String message) {
    }
}