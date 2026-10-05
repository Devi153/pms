package com.prepcv.pms.service;

import com.prepcv.pms.dto.CompanyRequest;
import com.prepcv.pms.dto.CompanyResponse;

import java.util.List;

public interface CompanyService {

    CompanyResponse createCompany(CompanyRequest request);

    List<CompanyResponse> getAllCompanies();

    CompanyResponse getCompanyById(Long companyId);

    CompanyResponse updateCompany(Long companyId, CompanyRequest request);

    void deleteCompany(Long companyId);
}