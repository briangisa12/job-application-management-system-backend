package com.example.job_application_management_system.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_application_management_system.entity.Company;
import com.example.job_application_management_system.repository.CompanyRepository;
import com.example.job_application_management_system.repository.RecruiterRepository;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    public String saveCompany(Company company) {
        companyRepository.save(company);
        return "Company saved successfully";
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(UUID id) {
        return companyRepository.findById(id);
    }

    public String updateCompany(UUID id, Company company) {
        Optional<Company> existing = companyRepository.findById(id);
        if (existing.isEmpty()) {
            return "The company with that id does not exist";
        }
        Company oldCompany = existing.get();
        oldCompany.setCompanyCode(company.getCompanyCode());
        oldCompany.setName(company.getName());
        companyRepository.save(oldCompany);
        return "Company updated successfully";
    }
    public String deleteCompany(UUID id) {
        Optional<Company> existing = companyRepository.findById(id);
        if (existing.isEmpty()) {
            return "The company with that id does not exist";
        }
        if (recruiterRepository.existsByCompany(existing.get())) {
            return "That company has a recruiter, move the recruiter first";
        }
        companyRepository.deleteById(id);
        return "Company deleted successfully";
    }

    public List<Company> getFreeCompanies() {
        return companyRepository.findFreeCompanies();
    }
}
