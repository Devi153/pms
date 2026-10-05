package com.prepcv.pms.service.impl;

import com.prepcv.pms.dto.PlacementDriveRequest;
import com.prepcv.pms.dto.PlacementDriveResponse;
import com.prepcv.pms.entity.Company;
import com.prepcv.pms.entity.DriveStatus;
import com.prepcv.pms.entity.PlacementDrive;
import com.prepcv.pms.exception.ResourceNotFoundException;
import com.prepcv.pms.repository.CompanyRepository;
import com.prepcv.pms.repository.PlacementDriveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlacementDriveServiceImplTest {

    @Mock
    private PlacementDriveRepository placementDriveRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private PlacementDriveServiceImpl placementDriveService;

    private Company company;
    private PlacementDrive drive;
    private PlacementDriveRequest request;

    @BeforeEach
    void setUp() {

        company = new Company();

        company.setCompanyId(1L);
        company.setCompanyName("TCS");
        company.setWebsite("https://www.tcs.com");
        company.setLocation("Chennai");

        drive = new PlacementDrive();

        drive.setDriveId(1L);
        drive.setCompany(company);
        drive.setJobTitle("Java Backend Developer");
        drive.setDescription(
                "Looking for Java and Spring Boot developers."
        );
        drive.setMinimumCgpa(
                new BigDecimal("7.50")
        );
        drive.setMinimumTenthPercentage(
                new BigDecimal("70.00")
        );
        drive.setMinimumTwelfthPercentage(
                new BigDecimal("65.00")
        );
        drive.setApplicationDeadline(
                LocalDate.now().plusDays(15)
        );
        drive.setDriveDate(
                LocalDate.now().plusDays(25)
        );
        drive.setStatus(DriveStatus.DRAFT);

        request = new PlacementDriveRequest();

        request.setCompanyId(1L);
        request.setJobTitle("Java Backend Developer");
        request.setDescription(
                "Looking for Java and Spring Boot developers."
        );
        request.setMinimumCgpa(
                new BigDecimal("7.50")
        );
        request.setMinimumTenthPercentage(
                new BigDecimal("70.00")
        );
        request.setMinimumTwelfthPercentage(
                new BigDecimal("65.00")
        );
        request.setApplicationDeadline(
                LocalDate.now().plusDays(15)
        );
        request.setDriveDate(
                LocalDate.now().plusDays(25)
        );
    }

    @Test
    void createDrive_shouldCreateSuccessfully() {

        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));

        when(placementDriveRepository.save(any(PlacementDrive.class)))
                .thenReturn(drive);

        PlacementDriveResponse response =
                placementDriveService.createDrive(request);

        assertNotNull(response);

        assertEquals(
                1L,
                response.getDriveId()
        );

        assertEquals(
                1L,
                response.getCompanyId()
        );

        assertEquals(
                "TCS",
                response.getCompanyName()
        );

        assertEquals(
                "Java Backend Developer",
                response.getJobTitle()
        );

        assertEquals(
                new BigDecimal("7.50"),
                response.getMinimumCgpa()
        );

        assertEquals(
                new BigDecimal("70.00"),
                response.getMinimumTenthPercentage()
        );

        assertEquals(
                new BigDecimal("65.00"),
                response.getMinimumTwelfthPercentage()
        );

        assertEquals(
                DriveStatus.DRAFT,
                response.getStatus()
        );

        verify(companyRepository, times(1))
                .findById(1L);

        verify(placementDriveRepository, times(1))
                .save(any(PlacementDrive.class));
    }

    @Test
    void createDrive_whenCompanyDoesNotExist_shouldThrowException() {

        when(companyRepository.findById(99L))
                .thenReturn(Optional.empty());

        request.setCompanyId(99L);

        assertThrows(
                ResourceNotFoundException.class,
                () -> placementDriveService.createDrive(request)
        );

        verify(companyRepository, times(1))
                .findById(99L);

        verify(placementDriveRepository, never())
                .save(any(PlacementDrive.class));
    }

    @Test
    void createDrive_whenDeadlineIsInPast_shouldThrowException() {

        request.setApplicationDeadline(
                LocalDate.now().minusDays(1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> placementDriveService.createDrive(request)
        );

        verify(companyRepository, never())
                .findById(anyLong());

        verify(placementDriveRepository, never())
                .save(any(PlacementDrive.class));
    }

    @Test
    void createDrive_whenDriveDateIsBeforeDeadline_shouldThrowException() {

        request.setApplicationDeadline(
                LocalDate.now().plusDays(20)
        );

        request.setDriveDate(
                LocalDate.now().plusDays(10)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> placementDriveService.createDrive(request)
        );

        verify(companyRepository, never())
                .findById(anyLong());

        verify(placementDriveRepository, never())
                .save(any(PlacementDrive.class));
    }

    @Test
    void getAllDrives_shouldReturnDrives() {

        when(placementDriveRepository.findAll())
                .thenReturn(List.of(drive));

        List<PlacementDriveResponse> response =
                placementDriveService.getAllDrives();

        assertNotNull(response);

        assertEquals(
                1,
                response.size()
        );

        assertEquals(
                "Java Backend Developer",
                response.get(0).getJobTitle()
        );

        assertEquals(
                "TCS",
                response.get(0).getCompanyName()
        );

        verify(placementDriveRepository, times(1))
                .findAll();
    }

    @Test
    void getAllDrives_whenNoDrives_shouldReturnEmptyList() {

        when(placementDriveRepository.findAll())
                .thenReturn(List.of());

        List<PlacementDriveResponse> response =
                placementDriveService.getAllDrives();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(placementDriveRepository, times(1))
                .findAll();
    }

    @Test
    void getDriveById_shouldReturnDrive() {

        when(placementDriveRepository.findById(1L))
                .thenReturn(Optional.of(drive));

        PlacementDriveResponse response =
                placementDriveService.getDriveById(1L);

        assertNotNull(response);

        assertEquals(
                1L,
                response.getDriveId()
        );

        assertEquals(
                "Java Backend Developer",
                response.getJobTitle()
        );

        assertEquals(
                "TCS",
                response.getCompanyName()
        );

        verify(placementDriveRepository, times(1))
                .findById(1L);
    }

    @Test
    void getDriveById_whenDriveDoesNotExist_shouldThrowException() {

        when(placementDriveRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> placementDriveService.getDriveById(99L)
        );

        verify(placementDriveRepository, times(1))
                .findById(99L);
    }

    @Test
    void updateDrive_shouldUpdateSuccessfully() {

        when(placementDriveRepository.findById(1L))
                .thenReturn(Optional.of(drive));

        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));

        when(placementDriveRepository.save(any(PlacementDrive.class)))
                .thenReturn(drive);

        PlacementDriveRequest updateRequest =
                new PlacementDriveRequest();

        updateRequest.setCompanyId(1L);
        updateRequest.setJobTitle(
                "Senior Java Backend Developer"
        );
        updateRequest.setDescription(
                "Updated Java Spring Boot position."
        );
        updateRequest.setMinimumCgpa(
                new BigDecimal("8.00")
        );
        updateRequest.setMinimumTenthPercentage(
                new BigDecimal("75.00")
        );
        updateRequest.setMinimumTwelfthPercentage(
                new BigDecimal("70.00")
        );
        updateRequest.setApplicationDeadline(
                LocalDate.now().plusDays(20)
        );
        updateRequest.setDriveDate(
                LocalDate.now().plusDays(30)
        );

        PlacementDriveResponse response =
                placementDriveService.updateDrive(
                        1L,
                        updateRequest
                );

        assertNotNull(response);

        verify(placementDriveRepository, times(1))
                .findById(1L);

        verify(companyRepository, times(1))
                .findById(1L);

        verify(placementDriveRepository, times(1))
                .save(any(PlacementDrive.class));
    }

    @Test
    void updateDrive_whenDriveDoesNotExist_shouldThrowException() {

        when(placementDriveRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> placementDriveService.updateDrive(
                        99L,
                        request
                )
        );

        verify(placementDriveRepository, times(1))
                .findById(99L);

        verify(companyRepository, never())
                .findById(anyLong());

        verify(placementDriveRepository, never())
                .save(any(PlacementDrive.class));
    }

    @Test
    void updateDrive_whenCompanyDoesNotExist_shouldThrowException() {

        when(placementDriveRepository.findById(1L))
                .thenReturn(Optional.of(drive));

        when(companyRepository.findById(99L))
                .thenReturn(Optional.empty());

        request.setCompanyId(99L);

        assertThrows(
                ResourceNotFoundException.class,
                () -> placementDriveService.updateDrive(
                        1L,
                        request
                )
        );

        verify(placementDriveRepository, times(1))
                .findById(1L);

        verify(companyRepository, times(1))
                .findById(99L);

        verify(placementDriveRepository, never())
                .save(any(PlacementDrive.class));
    }

    @Test
    void deleteDrive_shouldDeleteSuccessfully() {

        when(placementDriveRepository.findById(1L))
                .thenReturn(Optional.of(drive));

        placementDriveService.deleteDrive(1L);

        verify(placementDriveRepository, times(1))
                .findById(1L);

        verify(placementDriveRepository, times(1))
                .delete(drive);
    }

    @Test
    void deleteDrive_whenDriveDoesNotExist_shouldThrowException() {

        when(placementDriveRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> placementDriveService.deleteDrive(99L)
        );

        verify(placementDriveRepository, times(1))
                .findById(99L);

        verify(placementDriveRepository, never())
                .delete(any(PlacementDrive.class));
    }
}