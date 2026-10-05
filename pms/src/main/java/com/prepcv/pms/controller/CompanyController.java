package com.prepcv.pms.controller;

import com.prepcv.pms.dto.CompanyRequest;
import com.prepcv.pms.dto.CompanyResponse;
import com.prepcv.pms.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(
            @Valid @RequestBody CompanyRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(companyService.createCompany(request));
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {

        return ResponseEntity.ok(
                companyService.getAllCompanies()
        );
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompanyById(
            @PathVariable Long companyId) {

        return ResponseEntity.ok(
                companyService.getCompanyById(companyId)
        );
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Long companyId,
            @Valid @RequestBody CompanyRequest request) {

        return ResponseEntity.ok(
                companyService.updateCompany(
                        companyId,
                        request
                )
        );
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(
            @PathVariable Long companyId) {

        companyService.deleteCompany(companyId);

        return ResponseEntity.noContent().build();
    }
}