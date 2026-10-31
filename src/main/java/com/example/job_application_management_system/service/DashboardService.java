package com.example.job_application_management_system.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_application_management_system.entity.ApplicationStatus;
import com.example.job_application_management_system.repository.ApplicantRepository;
import com.example.job_application_management_system.repository.CompanyRepository;
import com.example.job_application_management_system.repository.InterviewRepository;
import com.example.job_application_management_system.repository.JobApplicationRepository;
import com.example.job_application_management_system.repository.RecruiterRepository;

@Service
public class DashboardService {
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private RecruiterRepository recruiterRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    @Autowired
    private InterviewRepository interviewRepository;

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("applicants", applicantRepository.count());
        summary.put("recruiters", recruiterRepository.count());
        summary.put("freeCompanies", (long) companyRepository.findFreeCompanies().size());
        summary.put("applicationsToday", jobApplicationRepository.countByApplicationDate(LocalDate.now()));
        summary.put("submitted", jobApplicationRepository.countByStatus(ApplicationStatus.SUBMITTED));
        summary.put("interviews", interviewRepository.count());
        return summary;
    }
}
