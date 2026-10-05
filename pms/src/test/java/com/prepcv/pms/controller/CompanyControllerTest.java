package com.prepcv.pms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prepcv.pms.dto.CompanyRequest;
import com.prepcv.pms.dto.CompanyResponse;
import com.prepcv.pms.exception.GlobalExceptionHandler;
import com.prepcv.pms.exception.ResourceNotFoundException;
import com.prepcv.pms.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
class CompanyControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        mockMvc = MockMvcBuilders
                .standaloneSetup(companyController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // =========================================================
    // CREATE COMPANY
    // =========================================================

    @Test
    void createCompany_shouldReturn201Created() throws Exception {

        CompanyRequest request = createValidRequest();

        CompanyResponse response = createResponse(1L);

        when(companyService.createCompany(any(CompanyRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/admin/companies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.companyId").value(1))
                .andExpect(jsonPath("$.companyName")
                        .value("Tata Consultancy Services"))
                .andExpect(jsonPath("$.website")
                        .value("https://www.tcs.com"))
                .andExpect(jsonPath("$.location")
                        .value("Chennai"));

        verify(companyService)
                .createCompany(any(CompanyRequest.class));
    }

    // =========================================================
    // GET ALL COMPANIES
    // =========================================================

    @Test
    void getAllCompanies_shouldReturn200Ok() throws Exception {

        CompanyResponse response1 = createResponse(1L);

        CompanyResponse response2 = new CompanyResponse(
                2L,
                "Infosys",
                "https://www.infosys.com",
                "Bangalore",
                LocalDateTime.of(2026, 9, 30, 15, 0)
        );

        when(companyService.getAllCompanies())
                .thenReturn(List.of(response1, response2));

        mockMvc.perform(
                        get("/api/admin/companies")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].companyId").value(1))
                .andExpect(jsonPath("$[0].companyName")
                        .value("Tata Consultancy Services"))
                .andExpect(jsonPath("$[1].companyId").value(2))
                .andExpect(jsonPath("$[1].companyName")
                        .value("Infosys"));

        verify(companyService).getAllCompanies();
    }

    // =========================================================
    // GET ALL COMPANIES - EMPTY
    // =========================================================

    @Test
    void getAllCompanies_whenNoCompanies_shouldReturnEmptyList()
            throws Exception {

        when(companyService.getAllCompanies())
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/api/admin/companies")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(companyService).getAllCompanies();
    }

    // =========================================================
    // GET COMPANY BY ID
    // =========================================================

    @Test
    void getCompanyById_shouldReturn200Ok() throws Exception {

        CompanyResponse response = createResponse(1L);

        when(companyService.getCompanyById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/admin/companies/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyId").value(1))
                .andExpect(jsonPath("$.companyName")
                        .value("Tata Consultancy Services"))
                .andExpect(jsonPath("$.website")
                        .value("https://www.tcs.com"))
                .andExpect(jsonPath("$.location")
                        .value("Chennai"));

        verify(companyService)
                .getCompanyById(1L);
    }

    // =========================================================
    // GET COMPANY BY ID - NOT FOUND
    // =========================================================

    @Test
    void getCompanyById_whenNotFound_shouldReturn404()
            throws Exception {

        when(companyService.getCompanyById(999L))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Company not found with id: 999"
                        )
                );

        mockMvc.perform(
                        get("/api/admin/companies/999")
                )
                .andExpect(status().isNotFound());

        verify(companyService)
                .getCompanyById(999L);
    }

    // =========================================================
    // UPDATE COMPANY
    // =========================================================

    @Test
    void updateCompany_shouldReturn200Ok() throws Exception {

        CompanyRequest request = createValidRequest();

        request.setCompanyName("TCS Technologies");
        request.setLocation("Pune");

        CompanyResponse response = new CompanyResponse(
                1L,
                "TCS Technologies",
                "https://www.tcs.com",
                "Pune",
                LocalDateTime.of(2026, 9, 30, 14, 37)
        );

        when(companyService.updateCompany(
                any(Long.class),
                any(CompanyRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/admin/companies/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyId").value(1))
                .andExpect(jsonPath("$.companyName")
                        .value("TCS Technologies"))
                .andExpect(jsonPath("$.location")
                        .value("Pune"));

        verify(companyService)
                .updateCompany(
                        any(Long.class),
                        any(CompanyRequest.class)
                );
    }

    // =========================================================
    // UPDATE COMPANY - NOT FOUND
    // =========================================================

    @Test
    void updateCompany_whenNotFound_shouldReturn404()
            throws Exception {

        CompanyRequest request = createValidRequest();

        when(companyService.updateCompany(
                any(Long.class),
                any(CompanyRequest.class)
        )).thenThrow(
                new ResourceNotFoundException(
                        "Company not found with id: 999"
                )
        );

        mockMvc.perform(
                        put("/api/admin/companies/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isNotFound());

        verify(companyService)
                .updateCompany(
                        any(Long.class),
                        any(CompanyRequest.class)
                );
    }

    // =========================================================
    // DELETE COMPANY
    // =========================================================

    @Test
    void deleteCompany_shouldReturn204NoContent()
            throws Exception {

        doNothing()
                .when(companyService)
                .deleteCompany(1L);

        mockMvc.perform(
                        delete("/api/admin/companies/1")
                )
                .andExpect(status().isNoContent());

        verify(companyService)
                .deleteCompany(1L);
    }

    // =========================================================
    // DELETE COMPANY - NOT FOUND
    // =========================================================

    @Test
    void deleteCompany_whenNotFound_shouldReturn404()
            throws Exception {

        doThrow(
                new ResourceNotFoundException(
                        "Company not found with id: 999"
                )
        )
                .when(companyService)
                .deleteCompany(999L);

        mockMvc.perform(
                        delete("/api/admin/companies/999")
                )
                .andExpect(status().isNotFound());

        verify(companyService)
                .deleteCompany(999L);
    }

    // =========================================================
    // CREATE COMPANY - NAME MISSING
    // =========================================================

    @Test
    void createCompany_whenNameMissing_shouldReturn400()
            throws Exception {

        CompanyRequest request = createValidRequest();

        request.setCompanyName("");

        mockMvc.perform(
                        post("/api/admin/companies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest());
    }

    // =========================================================
    // CREATE COMPANY - NAME TOO LONG
    // =========================================================

    @Test
    void createCompany_whenNameTooLong_shouldReturn400()
            throws Exception {

        CompanyRequest request = createValidRequest();

        request.setCompanyName("A".repeat(151));

        mockMvc.perform(
                        post("/api/admin/companies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest());
    }

    // =========================================================
    // HELPER: VALID REQUEST
    // =========================================================

    private CompanyRequest createValidRequest() {

        CompanyRequest request = new CompanyRequest();

        request.setCompanyName(
                "Tata Consultancy Services"
        );

        request.setWebsite(
                "https://www.tcs.com"
        );

        request.setLocation(
                "Chennai"
        );

        return request;
    }

    // =========================================================
    // HELPER: RESPONSE
    // =========================================================

    private CompanyResponse createResponse(Long companyId) {

        return new CompanyResponse(
                companyId,
                "Tata Consultancy Services",
                "https://www.tcs.com",
                "Chennai",
                LocalDateTime.of(
                        2026,
                        9,
                        30,
                        14,
                        37,
                        3
                )
        );
    }
}