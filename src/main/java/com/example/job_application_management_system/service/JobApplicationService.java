package com.example.job_application_management_system.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.job_application_management_system.entity.Applicant;
import com.example.job_application_management_system.entity.ApplicationStatus;
import com.example.job_application_management_system.entity.JobApplication;
import com.example.job_application_management_system.entity.Recruiter;
import com.example.job_application_management_system.repository.ApplicantRepository;
import com.example.job_application_management_system.repository.InterviewRepository;
import com.example.job_application_management_system.repository.JobApplicationRepository;
import com.example.job_application_management_system.repository.RecruiterRepository;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    private boolean applicantExists(Applicant applicant) {
        return applicant.getId() != null && applicantRepository.existsById(applicant.getId());
    }

    private boolean recruiterExists(Recruiter recruiter) {
        return recruiter.getId() != null && recruiterRepository.existsById(recruiter.getId());
    }

    public String saveJobApplication(JobApplication jobApplication) {
        if (jobApplication.getApplicationDate() == null) {
            return "Job application date is required";
        }
        if (jobApplication.getApplicationDate().isBefore(LocalDate.now())) {
            return "Job application date cannot be in the past";
        }

        if (jobApplication.getApplicant() == null) {
            return "Applicant is required";
        }
        if (jobApplication.getRecruiter() == null) {
            return "Recruiter is required";
        }
        if (!applicantExists(jobApplication.getApplicant())) {
            return "The applicant with that id does not exist";
        }
        if (!recruiterExists(jobApplication.getRecruiter())) {
            return "The recruiter with that id does not exist";
        }

        boolean alreadyHas = jobApplicationRepository.existsByRecruiterAndApplicationDateAndStatusNot(
                jobApplication.getRecruiter(),
                jobApplication.getApplicationDate(),
                ApplicationStatus.REJECTED);

        if (alreadyHas) {
            return "Recruiter already has an application on that date";
        }

        jobApplication.setStatus(ApplicationStatus.SUBMITTED);
        jobApplicationRepository.save(jobApplication);
        return "Job application saved successfully";
    }

    public List<JobApplication> getAllJobApplications() {
        return jobApplicationRepository.findAll();
    }

    public Optional<JobApplication> getJobApplicationById(UUID id) {
        return jobApplicationRepository.findById(id);
    }

    public String updateJobApplication(UUID id, JobApplication jobApplication) {
        Optional<JobApplication> existing = jobApplicationRepository.findById(id);
        if (existing.isEmpty()) {
            return "The job application with that id does not exist";
        }

        if (jobApplication.getApplicant() != null && !applicantExists(jobApplication.getApplicant())) {
            return "The applicant with that id does not exist";
        }
        if (jobApplication.getRecruiter() != null && !recruiterExists(jobApplication.getRecruiter())) {
            return "The recruiter with that id does not exist";
        }

        JobApplication old = existing.get();
        if (jobApplication.getApplicationDate() != null) {
            old.setApplicationDate(jobApplication.getApplicationDate());
        }
        if (jobApplication.getJobTitle() != null) {
            old.setJobTitle(jobApplication.getJobTitle());
        }
        if (jobApplication.getCvFileName() != null) {
            old.setCvFileName(jobApplication.getCvFileName());
        }
        if (jobApplication.getApplicant() != null) {
            old.setApplicant(jobApplication.getApplicant());
        }
        if (jobApplication.getRecruiter() != null) {
            old.setRecruiter(jobApplication.getRecruiter());
        }
        jobApplicationRepository.save(old);
        return "Job application updated successfully";
    }

    public String deleteJobApplication(UUID id) {
        Optional<JobApplication> existing = jobApplicationRepository.findById(id);
        if (existing.isEmpty()) {
            return "The job application with that id does not exist";
        }

        if (interviewRepository.existsByJobApplication(existing.get())) {
            return "That job application has interviews, delete them first";
        }

        jobApplicationRepository.deleteById(id);
        return "Job application deleted successfully";
    }

    public List<JobApplication> getJobApplicationsByStatus(ApplicationStatus status) {
        return jobApplicationRepository.findByStatusOrderByApplicationDateAsc(status);
    }

    public List<JobApplication> getJobApplicationsBetween(LocalDate start, LocalDate end) {
        return jobApplicationRepository.findByApplicationDateBetweenOrderByApplicationDateAsc(start, end);
    }

    public String updateStatus(UUID id, ApplicationStatus newStatus) {
        Optional<JobApplication> existing = jobApplicationRepository.findById(id);
        if (existing.isEmpty()) {
            return "The job application with that id does not exist";
        }

        JobApplication app = existing.get();
        ApplicationStatus current = app.getStatus();

        boolean allowed = false;

        if (current == ApplicationStatus.SUBMITTED) {
            if (newStatus == ApplicationStatus.SCREENED || newStatus == ApplicationStatus.REJECTED) {
                allowed = true;
            }
        } else if (current == ApplicationStatus.SCREENED) {
            if (newStatus == ApplicationStatus.SHORTLISTED || newStatus == ApplicationStatus.REJECTED) {
                allowed = true;
            }
        }

        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }

        app.setStatus(newStatus);
        jobApplicationRepository.save(app);
        return "Job application status updated successfully";
    }

    public Page<JobApplication> getJobApplicationsPage(Pageable pageable) {
        return jobApplicationRepository.findAll(pageable);
    }

    public List<Object[]> getStatsByStatus() {
        return jobApplicationRepository.countApplicationsPerStatus();
    }

    @Transactional
    public String cancelDay(UUID recruiterId, LocalDate date) {
        int count = jobApplicationRepository.cancelDay(
                recruiterId, date, ApplicationStatus.REJECTED, ApplicationStatus.SHORTLISTED);
        return count + " job applications cancelled";
    }
}
