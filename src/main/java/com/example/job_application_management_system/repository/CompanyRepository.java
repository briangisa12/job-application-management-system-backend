package com.example.job_application_management_system.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.job_application_management_system.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Query("SELECT c FROM Company c WHERE NOT EXISTS (SELECT r FROM Recruiter r WHERE r.company = c) ORDER BY c.companyCode")
    List<Company> findFreeCompanies();
    Optional<Company> findByCompanyCode(int companyCode);

}
