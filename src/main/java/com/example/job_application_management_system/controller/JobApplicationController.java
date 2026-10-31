package com.example.job_application_management_system.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.job_application_management_system.entity.ApplicationStatus;
import com.example.job_application_management_system.entity.JobApplication;
import com.example.job_application_management_system.service.JobApplicationService;

@RestController
@RequestMapping("/api/job-applications")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleBadDate() {
        return new ResponseEntity<>("Invalid date, use the format yyyy-MM-dd", HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<String> handleBadSortField() {
        return new ResponseEntity<>("Invalid sort field", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveJobApplication(@RequestBody JobApplication jobApplication) {
        String message = jobApplicationService.saveJobApplication(jobApplication);
        if (message.equals("Job application date cannot be in the past")
                || message.equals("Job application date is required")
                || message.equals("Applicant is required")
                || message.equals("Recruiter is required")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else if (message.equals("The applicant with that id does not exist")
                || message.equals("The recruiter with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("Recruiter already has an application on that date")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobApplication>> getAllJobApplications() {
        List<JobApplication> list = jobApplicationService.getAllJobApplications();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @GetMapping("/by-status")
    public ResponseEntity<List<JobApplication>> getJobApplicationsByStatus(@RequestParam ApplicationStatus status) {
        List<JobApplication> list = jobApplicationService.getJobApplicationsByStatus(status);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @GetMapping("/between")
    public ResponseEntity<List<JobApplication>> getJobApplicationsBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        List<JobApplication> list = jobApplicationService.getJobApplicationsBetween(startDate, endDate);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @GetMapping("/page")
    public ResponseEntity<Page<JobApplication>> getJobApplicationsPage(@RequestParam int page, @RequestParam int size,
         @RequestParam String sort) {
        String[] sortParts = sort.split(",");
        String field = sortParts[0];
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, field));
        Page<JobApplication> result = jobApplicationService.getJobApplicationsPage(pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> getStatsByStatus() {
        List<Object[]> stats = jobApplicationService.getStatsByStatus();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam UUID recruiterId, @RequestParam String date) {
        LocalDate day = LocalDate.parse(date);
        String message = jobApplicationService.cancelDay(recruiterId, day);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobApplicationById(@PathVariable UUID id) {
        Optional<JobApplication> jobApplication = jobApplicationService.getJobApplicationById(id);
        if (jobApplication.isPresent()) {
            return new ResponseEntity<>(jobApplication.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The job application with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateJobApplication(@PathVariable UUID id, @RequestBody JobApplication jobApplication) {
        String message = jobApplicationService.updateJobApplication(id, jobApplication);
        if (message.equals("The job application with that id does not exist")
                || message.equals("The applicant with that id does not exist")
                || message.equals("The recruiter with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable UUID id, @RequestParam ApplicationStatus status) {
        String message = jobApplicationService.updateStatus(id, status);
        if (message.equals("The job application with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.startsWith("Cannot change status")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteJobApplication(@PathVariable UUID id) {
        String message = jobApplicationService.deleteJobApplication(id);
        if (message.equals("The job application with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("That job application has interviews, delete them first")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
