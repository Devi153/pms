package com.prepcv.pms.service.impl;

import com.prepcv.pms.dto.CompanyRequest;
import com.prepcv.pms.dto.CompanyResponse;
import com.prepcv.pms.entity.Company;
import com.prepcv.pms.exception.ResourceNotFoundException;
import com.prepcv.pms.repository.CompanyRepository;
import com.prepcv.pms.service.CompanyService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public CompanyResponse createCompany(CompanyRequest request) {

        Company company = new Company();

        company.setCompanyName(request.getCompanyName().trim());
        company.setWebsite(request.getWebsite());
        company.setLocation(request.getLocation());
        company.setCreatedAt(LocalDateTime.now());

        Company savedCompany = companyRepository.save(company);

        return mapToResponse(savedCompany);
    }

    @Override
    public List<CompanyResponse> getAllCompanies() {

        return companyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CompanyResponse getCompanyById(Long companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with id: " + companyId
                        ));

        return mapToResponse(company);
    }

    @Override
    public CompanyResponse updateCompany(
            Long companyId,
            CompanyRequest request) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with id: " + companyId
                        ));

        company.setCompanyName(request.getCompanyName().trim());
        company.setWebsite(request.getWebsite());
        company.setLocation(request.getLocation());

        Company updatedCompany = companyRepository.save(company);

        return mapToResponse(updatedCompany);
    }

    @Override
    public void deleteCompany(Long companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with id: " + companyId
                        ));

        companyRepository.delete(company);
    }

    private CompanyResponse mapToResponse(Company company) {

        return new CompanyResponse(
                company.getCompanyId(),
                company.getCompanyName(),
                company.getWebsite(),
                company.getLocation(),
                company.getCreatedAt()
        );
    }
}