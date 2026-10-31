package com.example.job_application_management_system.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_application_management_system.entity.JobField;
import com.example.job_application_management_system.repository.JobFieldRepository;
import com.example.job_application_management_system.repository.RecruiterRepository;

@Service
public class JobFieldService {
    @Autowired
    private JobFieldRepository jobFieldRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    public String saveJobField(JobField jobField) {
        jobFieldRepository.save(jobField);
        return "Job field saved successfully";
    }

    public List<JobField> getAllJobFields() {
        return jobFieldRepository.findAll();
    }

    public Optional<JobField> getJobFieldById(UUID id) {
        return jobFieldRepository.findById(id);
    }

    public String updateJobField(UUID id, JobField jobField) {
        Optional<JobField> existing = jobFieldRepository.findById(id);
        if (existing.isEmpty()) {
            return "The job field with that id does not exist";
        }
        JobField oldJobField = existing.get();
        oldJobField.setName(jobField.getName());
        jobFieldRepository.save(oldJobField);
        return "Job field updated successfully";
    }

    public String deleteJobField(UUID id) {
        Optional<JobField> existing = jobFieldRepository.findById(id);
        if (existing.isEmpty()) {
            return "The job field with that id does not exist";
        }
        if (recruiterRepository.existsByJobFields(existing.get())) {
            return "That job field is held by a recruiter";
        }
        jobFieldRepository.deleteById(id);
        return "Job field deleted successfully";
    }

    public List<JobField> getUnusedJobFields() {
        return jobFieldRepository.findUnusedJobFields();
    }
}
