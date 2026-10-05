package com.prepcv.pms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prepcv.pms.dto.PlacementDriveRequest;
import com.prepcv.pms.dto.PlacementDriveResponse;
import com.prepcv.pms.entity.DriveStatus;
import com.prepcv.pms.exception.GlobalExceptionHandler;
import com.prepcv.pms.exception.ResourceNotFoundException;
import com.prepcv.pms.service.PlacementDriveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PlacementDriveControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private PlacementDriveService placementDriveService;

    @InjectMocks
    private PlacementDriveController placementDriveController;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        mockMvc = MockMvcBuilders
                .standaloneSetup(placementDriveController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // =========================================================
    // CREATE DRIVE
    // =========================================================

    @Test
    void createDrive_shouldReturn201Created() throws Exception {

        PlacementDriveRequest request = createValidRequest();

        PlacementDriveResponse response = createResponse(1L);

        when(placementDriveService.createDrive(any(PlacementDriveRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/admin/drives")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.driveId").value(1))
                .andExpect(jsonPath("$.companyId").value(1))
                .andExpect(jsonPath("$.companyName")
                        .value("Tata Consultancy Services"))
                .andExpect(jsonPath("$.jobTitle")
                        .value("Java Backend Developer"))
                .andExpect(jsonPath("$.minimumCgpa").value(7.0))
                .andExpect(jsonPath("$.minimumTenthPercentage").value(70.0))
                .andExpect(jsonPath("$.minimumTwelfthPercentage").value(70.0))
                .andExpect(jsonPath("$.status").value("DRAFT"));

        verify(placementDriveService)
                .createDrive(any(PlacementDriveRequest.class));
    }

    // =========================================================
    // GET ALL DRIVES
    // =========================================================

    @Test
    void getAllDrives_shouldReturn200Ok() throws Exception {

        PlacementDriveResponse response1 = createResponse(1L);
        PlacementDriveResponse response2 = createResponse(2L);

        when(placementDriveService.getAllDrives())
                .thenReturn(List.of(response1, response2));

        mockMvc.perform(
                        get("/api/admin/drives")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].driveId").value(1))
                .andExpect(jsonPath("$[1].driveId").value(2));

        verify(placementDriveService).getAllDrives();
    }

    // =========================================================
    // GET ALL DRIVES - EMPTY
    // =========================================================

    @Test
    void getAllDrives_whenNoDrives_shouldReturnEmptyList() throws Exception {

        when(placementDriveService.getAllDrives())
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/api/admin/drives")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(placementDriveService).getAllDrives();
    }

    // =========================================================
    // GET DRIVE BY ID
    // =========================================================

    @Test
    void getDriveById_shouldReturn200Ok() throws Exception {

        PlacementDriveResponse response = createResponse(1L);

        when(placementDriveService.getDriveById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/admin/drives/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driveId").value(1))
                .andExpect(jsonPath("$.companyId").value(1))
                .andExpect(jsonPath("$.companyName")
                        .value("Tata Consultancy Services"))
                .andExpect(jsonPath("$.jobTitle")
                        .value("Java Backend Developer"));

        verify(placementDriveService)
                .getDriveById(1L);
    }

    // =========================================================
    // GET DRIVE BY ID - NOT FOUND
    // =========================================================

    @Test
    void getDriveById_whenNotFound_shouldReturn404() throws Exception {

        when(placementDriveService.getDriveById(999L))
                .thenThrow(new ResourceNotFoundException(
                        "Placement drive not found with id: 999"
                ));

        mockMvc.perform(
                        get("/api/admin/drives/999")
                )
                .andExpect(status().isNotFound());

        verify(placementDriveService)
                .getDriveById(999L);
    }

    // =========================================================
    // UPDATE DRIVE
    // =========================================================

    @Test
    void updateDrive_shouldReturn200Ok() throws Exception {

        PlacementDriveRequest request = createValidRequest();

        request.setJobTitle("Senior Java Backend Developer");
        request.setMinimumCgpa(new BigDecimal("7.50"));

        PlacementDriveResponse response = createResponse(1L);

        response.setJobTitle("Senior Java Backend Developer");
        response.setMinimumCgpa(new BigDecimal("7.50"));

        when(placementDriveService.updateDrive(
                eq(1L),
                any(PlacementDriveRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/admin/drives/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driveId").value(1))
                .andExpect(jsonPath("$.jobTitle")
                        .value("Senior Java Backend Developer"))
                .andExpect(jsonPath("$.minimumCgpa").value(7.5));

        verify(placementDriveService)
                .updateDrive(
                        eq(1L),
                        any(PlacementDriveRequest.class)
                );
    }

    // =========================================================
    // UPDATE DRIVE - NOT FOUND
    // =========================================================

    @Test
    void updateDrive_whenNotFound_shouldReturn404() throws Exception {

        PlacementDriveRequest request = createValidRequest();

        when(placementDriveService.updateDrive(
                eq(999L),
                any(PlacementDriveRequest.class)
        )).thenThrow(new ResourceNotFoundException(
                "Placement drive not found with id: 999"
        ));

        mockMvc.perform(
                        put("/api/admin/drives/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());

        verify(placementDriveService)
                .updateDrive(
                        eq(999L),
                        any(PlacementDriveRequest.class)
                );
    }

    // =========================================================
    // DELETE DRIVE
    // =========================================================

    @Test
    void deleteDrive_shouldReturn204NoContent() throws Exception {

        doNothing()
                .when(placementDriveService)
                .deleteDrive(1L);

        mockMvc.perform(
                        delete("/api/admin/drives/1")
                )
                .andExpect(status().isNoContent());

        verify(placementDriveService)
                .deleteDrive(1L);
    }

    // =========================================================
    // DELETE DRIVE - NOT FOUND
    // =========================================================

    @Test
    void deleteDrive_whenNotFound_shouldReturn404() throws Exception {

        doThrow(new ResourceNotFoundException(
                "Placement drive not found with id: 999"
        ))
                .when(placementDriveService)
                .deleteDrive(999L);

        mockMvc.perform(
                        delete("/api/admin/drives/999")
                )
                .andExpect(status().isNotFound());

        verify(placementDriveService)
                .deleteDrive(999L);
    }

    // =========================================================
    // CREATE DRIVE - VALIDATION FAILURE
    // =========================================================

    @Test
    void createDrive_whenCompanyIdMissing_shouldReturn400() throws Exception {

        PlacementDriveRequest request = createValidRequest();

        request.setCompanyId(null);

        mockMvc.perform(
                        post("/api/admin/drives")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    // =========================================================
    // CREATE DRIVE - JOB TITLE MISSING
    // =========================================================

    @Test
    void createDrive_whenJobTitleMissing_shouldReturn400() throws Exception {

        PlacementDriveRequest request = createValidRequest();

        request.setJobTitle("");

        mockMvc.perform(
                        post("/api/admin/drives")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    // =========================================================
    // CREATE DRIVE - DEADLINE MISSING
    // =========================================================

    @Test
    void createDrive_whenDeadlineMissing_shouldReturn400() throws Exception {

        PlacementDriveRequest request = createValidRequest();

        request.setApplicationDeadline(null);

        mockMvc.perform(
                        post("/api/admin/drives")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    // =========================================================
    // HELPER: VALID REQUEST
    // =========================================================

    private PlacementDriveRequest createValidRequest() {

        PlacementDriveRequest request =
                new PlacementDriveRequest();

        request.setCompanyId(1L);
        request.setJobTitle("Java Backend Developer");
        request.setDescription(
                "Backend development using Java and Spring Boot"
        );
        request.setMinimumCgpa(
                new BigDecimal("7.00")
        );
        request.setMinimumTenthPercentage(
                new BigDecimal("70.00")
        );
        request.setMinimumTwelfthPercentage(
                new BigDecimal("70.00")
        );
        request.setApplicationDeadline(
                LocalDate.of(2026, 10, 15)
        );
        request.setDriveDate(
                LocalDate.of(2026, 10, 20)
        );

        return request;
    }

    // =========================================================
    // HELPER: RESPONSE
    // =========================================================

    private PlacementDriveResponse createResponse(Long driveId) {

        return new PlacementDriveResponse(
                driveId,
                1L,
                "Tata Consultancy Services",
                "Java Backend Developer",
                "Backend development using Java and Spring Boot",
                new BigDecimal("7.00"),
                new BigDecimal("70.00"),
                new BigDecimal("70.00"),
                LocalDate.of(2026, 10, 15),
                LocalDate.of(2026, 10, 20),
                DriveStatus.DRAFT
        );
    }
}