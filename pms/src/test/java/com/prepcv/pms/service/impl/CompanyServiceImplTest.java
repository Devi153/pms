package com.prepcv.pms.service.impl;

import com.prepcv.pms.dto.CompanyRequest;
import com.prepcv.pms.dto.CompanyResponse;
import com.prepcv.pms.entity.Company;
import com.prepcv.pms.exception.ResourceNotFoundException;
import com.prepcv.pms.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company company;
    private CompanyRequest request;

    @BeforeEach
    void setUp() {

        company = new Company();

        company.setCompanyId(1L);
        company.setCompanyName("TCS");
        company.setWebsite("https://www.tcs.com");
        company.setLocation("Chennai");
        company.setCreatedAt(LocalDateTime.now());

        request = new CompanyRequest();

        request.setCompanyName("TCS");
        request.setWebsite("https://www.tcs.com");
        request.setLocation("Chennai");
    }

    @Test
    void createCompany_shouldCreateSuccessfully() {

        when(companyRepository.save(any(Company.class)))
                .thenReturn(company);

        CompanyResponse response =
                companyService.createCompany(request);

        assertNotNull(response);
        assertEquals(1L, response.getCompanyId());
        assertEquals("TCS", response.getCompanyName());
        assertEquals("https://www.tcs.com", response.getWebsite());
        assertEquals("Chennai", response.getLocation());

        verify(companyRepository, times(1))
                .save(any(Company.class));
    }

    @Test
    void getAllCompanies_shouldReturnCompanies() {

        when(companyRepository.findAll())
                .thenReturn(List.of(company));

        List<CompanyResponse> response =
                companyService.getAllCompanies();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("TCS", response.get(0).getCompanyName());

        verify(companyRepository, times(1))
                .findAll();
    }

    @Test
    void getAllCompanies_whenNoCompanies_shouldReturnEmptyList() {

        when(companyRepository.findAll())
                .thenReturn(List.of());

        List<CompanyResponse> response =
                companyService.getAllCompanies();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(companyRepository, times(1))
                .findAll();
    }

    @Test
    void getCompanyById_shouldReturnCompany() {

        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));

        CompanyResponse response =
                companyService.getCompanyById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getCompanyId());
        assertEquals("TCS", response.getCompanyName());

        verify(companyRepository, times(1))
                .findById(1L);
    }

    @Test
    void getCompanyById_whenCompanyDoesNotExist_shouldThrowException() {

        when(companyRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> companyService.getCompanyById(99L)
        );

        verify(companyRepository, times(1))
                .findById(99L);
    }

    @Test
    void updateCompany_shouldUpdateSuccessfully() {

        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));

        when(companyRepository.save(any(Company.class)))
                .thenReturn(company);

        CompanyRequest updateRequest =
                new CompanyRequest();

        updateRequest.setCompanyName("TCS Updated");
        updateRequest.setWebsite("https://updated.tcs.com");
        updateRequest.setLocation("Bangalore");

        CompanyResponse response =
                companyService.updateCompany(
                        1L,
                        updateRequest
                );

        assertNotNull(response);
        assertEquals(
                "TCS Updated",
                response.getCompanyName()
        );
        assertEquals(
                "https://updated.tcs.com",
                response.getWebsite()
        );
        assertEquals(
                "Bangalore",
                response.getLocation()
        );

        verify(companyRepository, times(1))
                .findById(1L);

        verify(companyRepository, times(1))
                .save(any(Company.class));
    }

    @Test
    void updateCompany_whenCompanyDoesNotExist_shouldThrowException() {

        when(companyRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> companyService.updateCompany(
                        99L,
                        request
                )
        );

        verify(companyRepository, times(1))
                .findById(99L);

        verify(companyRepository, never())
                .save(any(Company.class));
    }

    @Test
    void deleteCompany_shouldDeleteSuccessfully() {

        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));

        companyService.deleteCompany(1L);

        verify(companyRepository, times(1))
                .findById(1L);

        verify(companyRepository, times(1))
                .delete(company);
    }

    @Test
    void deleteCompany_whenCompanyDoesNotExist_shouldThrowException() {

        when(companyRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> companyService.deleteCompany(99L)
        );

        verify(companyRepository, times(1))
                .findById(99L);

        verify(companyRepository, never())
                .delete(any(Company.class));
    }
}