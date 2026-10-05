package com.prepcv.pms.repository;

import com.prepcv.pms.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}