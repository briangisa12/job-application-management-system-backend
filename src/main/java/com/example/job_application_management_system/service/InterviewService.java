package com.example.job_application_management_system.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_application_management_system.entity.ApplicationStatus;
import com.example.job_application_management_system.entity.Interview;
import com.example.job_application_management_system.entity.JobApplication;
import com.example.job_application_management_system.repository.InterviewRepository;
import com.example.job_application_management_system.repository.JobApplicationRepository;

@Service
public class InterviewService {
    @Autowired
    private InterviewRepository interviewRepository;
    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    public String saveInterview(UUID jobApplicationId, Interview interview) {
        Optional<JobApplication> jobApplicationOptional = jobApplicationRepository.findById(jobApplicationId);
        if (jobApplicationOptional.isEmpty()) {
            return "The job application with that id does not exist";
        }
        JobApplication jobApplication = jobApplicationOptional.get();
        if (jobApplication.getStatus() != ApplicationStatus.SHORTLISTED) {
            return "Interviews are only for SHORTLISTED job applications";
        }

        interview.setInterviewDate(LocalDate.now());
        interview.setJobApplication(jobApplication);
        interviewRepository.save(interview);
        return "Interview saved successfully";
    }

    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    public Optional<Interview> getInterviewById(UUID id) {
        return interviewRepository.findById(id);
    }

    public List<Interview> getInterviewsOfApplicant(String nationalId) {
        return interviewRepository.findInterviewsOfApplicant(nationalId);
    }

    public String deleteInterview(UUID id) {
        if (!interviewRepository.existsById(id)) {
            return "The interview with that id does not exist";
        }
        interviewRepository.deleteById(id);
        return "Interview deleted successfully";
    }
}
