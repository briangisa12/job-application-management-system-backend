package com.example.job_application_management_system.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.job_application_management_system.entity.Interview;
import com.example.job_application_management_system.service.InterviewService;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;
    @PostMapping("/save")
    public ResponseEntity<String> saveInterview(@RequestParam UUID jobApplicationId, @RequestBody Interview interview) {
        String message = interviewService.saveInterview(jobApplicationId, interview);
        if (message.equals("The job application with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("Interviews are only for SHORTLISTED job applications")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<Interview>> getAllInterviews() {
        return new ResponseEntity<>(interviewService.getAllInterviews(), HttpStatus.OK);
    }

    @GetMapping("/applicant/{nationalId}")
    public ResponseEntity<List<Interview>> getInterviewsOfApplicant(@PathVariable String nationalId) {
        List<Interview> interviews = interviewService.getInterviewsOfApplicant(nationalId);
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInterviewById(@PathVariable UUID id) {
        Optional<Interview> interview = interviewService.getInterviewById(id);
        if (interview.isPresent()) {
            return new ResponseEntity<>(interview.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The interview with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInterview(@PathVariable UUID id) {
        String message = interviewService.deleteInterview(id);
        if (message.equals("The interview with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
