package com.example.job_application_management_system.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_application_management_system.entity.Company;
import com.example.job_application_management_system.entity.JobField;
import com.example.job_application_management_system.entity.Recruiter;
import com.example.job_application_management_system.repository.CompanyRepository;
import com.example.job_application_management_system.repository.JobApplicationRepository;
import com.example.job_application_management_system.repository.JobFieldRepository;
import com.example.job_application_management_system.repository.RecruiterRepository;

@Service
public class RecruiterService {
    @Autowired
    private RecruiterRepository recruiterRepository;
    @Autowired
    private JobFieldRepository jobFieldRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    private String checkReferences(Recruiter recruiter) {
        Company company = recruiter.getCompany();
        if (company != null && (company.getId() == null || !companyRepository.existsById(company.getId()))) {
            return "The company with that id does not exist";
        }
        if (recruiter.getJobFields() != null) {
            for (JobField jobField : recruiter.getJobFields()) {
                if (jobField.getId() == null || !jobFieldRepository.existsById(jobField.getId())) {
                    return "That job field does not exist";
                }
            }
        }
        return null;
    }

    public String saveRecruiter(Recruiter recruiter) {
        String error = checkReferences(recruiter);
        if (error != null) {
            return error;
        }
        recruiterRepository.save(recruiter);
        return "Recruiter saved successfully";
    }

    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }

    public Optional<Recruiter> getRecruiterById(UUID id) {
        return recruiterRepository.findById(id);
    }

    public String updateRecruiter(UUID id, Recruiter recruiter) {
        Optional<Recruiter> existing = recruiterRepository.findById(id);
        if (existing.isEmpty()) {
            return "The recruiter with that id does not exist";
        }
        String error = checkReferences(recruiter);
        if (error != null) {
            return error;
        }
        Recruiter oldRecruiter = existing.get();
        oldRecruiter.setFirstName(recruiter.getFirstName());
        oldRecruiter.setLastName(recruiter.getLastName());
        oldRecruiter.setEmail(recruiter.getEmail());
        oldRecruiter.setCompany(recruiter.getCompany());
        oldRecruiter.setJobFields(recruiter.getJobFields());
        recruiterRepository.save(oldRecruiter);
        return "Recruiter updated successfully";
    }

    public String deleteRecruiter(UUID id) {
        Optional<Recruiter> existing = recruiterRepository.findById(id);
        if (existing.isEmpty()) {
            return "The recruiter with that id does not exist";
        }
        if (jobApplicationRepository.existsByRecruiter(existing.get())) {
            return "That recruiter has job applications, delete them first";
        }
        recruiterRepository.deleteById(id);
        return "Recruiter deleted successfully";
    }

    public List<Recruiter> getRecruitersByJobField(String name) {
        return recruiterRepository.findRecruitersByJobField(name);
    }
    public String addJobFieldToRecruiter(UUID recruiterId, UUID jobFieldId) {
        Optional<Recruiter> recruiterOptional = recruiterRepository.findById(recruiterId);
        if (recruiterOptional.isEmpty()) {
            return "The recruiter with that id does not exist";
        }
        Optional<JobField> jobFieldOptional = jobFieldRepository.findById(jobFieldId);
        if (jobFieldOptional.isEmpty()) {
            return "That job field does not exist";
        }

        Recruiter recruiter = recruiterOptional.get();
        JobField jobField = jobFieldOptional.get();

        if (recruiter.getJobFields().contains(jobField)) {
            return "The recruiter already has that job field";
        }
        recruiter.getJobFields().add(jobField);
        recruiterRepository.save(recruiter);
        return "Job field added to the recruiter";
    }
    public String changeCompany(UUID recruiterId, int companyCode) {
        Optional<Recruiter> recruiterOptional = recruiterRepository.findById(recruiterId);
        if (recruiterOptional.isEmpty()) {
            return "The recruiter with that id does not exist";
        }
        Optional<Company> companyOptional = companyRepository.findByCompanyCode(companyCode);
        if (companyOptional.isEmpty()) {
            return "The company with that companyCode does not exist";
        }

        Recruiter recruiter = recruiterOptional.get();
        Company company = companyOptional.get();

        if (recruiter.getCompany() != null && recruiter.getCompany().getId().equals(company.getId())) {
            return "Company changed successfully";
        }

        if (recruiterRepository.existsByCompany(company)) {
            return "That company is taken by another recruiter";
        }

        recruiter.setCompany(company);
        recruiterRepository.save(recruiter);
        return "Company changed successfully";
    }
}
