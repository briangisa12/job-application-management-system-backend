package com.example.job_application_management_system.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_application_management_system.entity.Applicant;
import com.example.job_application_management_system.repository.ApplicantRepository;
import com.example.job_application_management_system.repository.JobApplicationRepository;

@Service
public class ApplicantService {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    public String saveApplicant(Applicant applicant) {
        if (applicant.getNationalId() != null && applicantRepository.existsByNationalId(applicant.getNationalId())) {
            return "An applicant with that nationalId already exists";
        }
        applicantRepository.save(applicant);
        return "Applicant saved successfully";
    }

    public List<Applicant> getAllApplicants() {
        return applicantRepository.findAll();
    }

    public Optional<Applicant> getApplicantById(UUID id) {
        return applicantRepository.findById(id);
    }

    public String updateApplicant(UUID id, Applicant applicant) {
        Optional<Applicant> existing = applicantRepository.findById(id);
        if (existing.isEmpty()) {
            return "The applicant with that id does not exist";
        }
        if (applicant.getNationalId() != null && applicantRepository.existsByNationalIdAndIdNot(applicant.getNationalId(), id)) {
            return "An applicant with that nationalId already exists";
        }
        Applicant oldApplicant = existing.get();
        oldApplicant.setFirstName(applicant.getFirstName());
        oldApplicant.setLastName(applicant.getLastName());
        oldApplicant.setNationalId(applicant.getNationalId());
        oldApplicant.setEmail(applicant.getEmail());
        oldApplicant.setPhone(applicant.getPhone());
        applicantRepository.save(oldApplicant);
        return "Applicant updated successfully";
    }

    public String deleteApplicant(UUID id) {
        Optional<Applicant> existing = applicantRepository.findById(id);
        if (existing.isEmpty()) {
            return "The applicant with that id does not exist";
        }
        if (jobApplicationRepository.existsByApplicant(existing.get())) {
            return "That applicant has job applications, delete them first";
        }
        applicantRepository.deleteById(id);
        return "Applicant deleted successfully";
    }

    public List<Applicant> getApplicantsByLastName(String lastName) {
        return applicantRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Applicant> getApplicantsOfRecruiter(UUID recruiterId) {
        return applicantRepository.findApplicantsOfRecruiter(recruiterId);
    }
}
