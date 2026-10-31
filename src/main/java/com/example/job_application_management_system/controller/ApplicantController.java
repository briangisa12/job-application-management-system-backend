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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.job_application_management_system.entity.Applicant;
import com.example.job_application_management_system.service.ApplicantService;
import com.example.job_application_management_system.service.RecruiterService;

@RestController
@RequestMapping("/api/applicants")
public class ApplicantController {

    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private RecruiterService recruiterService;

    @PostMapping("/save")
    public ResponseEntity<String> saveApplicant(@RequestBody Applicant applicant) {
    String message = applicantService.saveApplicant(applicant);
    if (message.equals("An applicant with that nationalId already exists")) {
     return new ResponseEntity<>(message, HttpStatus.CONFLICT);
     } else {
     return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Applicant>> getAllApplicants() {
     List<Applicant> applicants = applicantService.getAllApplicants();
     return new ResponseEntity<>(applicants, HttpStatus.OK);
    }

    
    @GetMapping("/by-last-name")
    public ResponseEntity<List<Applicant>> getApplicantsByLastName(@RequestParam String lastName) {
     List<Applicant> applicants = applicantService.getApplicantsByLastName(lastName);
     return new ResponseEntity<>(applicants, HttpStatus.OK);
    }

    @GetMapping("/of-recruiter/{recruiterId}")
    public ResponseEntity<?> getApplicantsOfRecruiter(@PathVariable UUID recruiterId) {
       
     if (recruiterService.getRecruiterById(recruiterId).isEmpty()) {
         return new ResponseEntity<>("The recruiter with that id does not exist", HttpStatus.NOT_FOUND);
     }
    List<Applicant> applicants = applicantService.getApplicantsOfRecruiter(recruiterId);
        return new ResponseEntity<>(applicants, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getApplicantById(@PathVariable UUID id) {
        Optional<Applicant> applicant = applicantService.getApplicantById(id);
        if (applicant.isPresent()) {
            return new ResponseEntity<>(applicant.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The applicant with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateApplicant(@PathVariable UUID id, @RequestBody Applicant applicant) {
        String message = applicantService.updateApplicant(id, applicant);
        if (message.equals("The applicant with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("An applicant with that nationalId already exists")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteApplicant(@PathVariable UUID id) {
        String message = applicantService.deleteApplicant(id);
        if (message.equals("The applicant with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("That applicant has job applications, delete them first")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
