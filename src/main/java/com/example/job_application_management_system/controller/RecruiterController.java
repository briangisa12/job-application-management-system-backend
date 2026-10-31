package com.example.job_application_management_system.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.job_application_management_system.entity.Recruiter;
import com.example.job_application_management_system.service.RecruiterService;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    @PostMapping("/save")
    public ResponseEntity<String> saveRecruiter(@RequestBody Recruiter recruiter) {
        String message = recruiterService.saveRecruiter(recruiter);
        if (message.equals("The company with that id does not exist") || message.equals("That job field does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Recruiter>> getAllRecruiters() {
        List<Recruiter> recruiters = recruiterService.getAllRecruiters();
        return new ResponseEntity<>(recruiters, HttpStatus.OK);
    }
    @GetMapping("/by-job-field")
    public ResponseEntity<List<Recruiter>> getRecruitersByJobField(@RequestParam String name) {
        List<Recruiter> recruiters = recruiterService.getRecruitersByJobField(name);
        return new ResponseEntity<>(recruiters, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecruiterById(@PathVariable UUID id) {
        Optional<Recruiter> recruiter = recruiterService.getRecruiterById(id);
        if (recruiter.isPresent()) {
            return new ResponseEntity<>(recruiter.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The recruiter with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRecruiter(@PathVariable UUID id, @RequestBody Recruiter recruiter) {
        String message = recruiterService.updateRecruiter(id, recruiter);
        if (message.equals("The recruiter with that id does not exist")
                || message.equals("The company with that id does not exist")
                || message.equals("That job field does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @PutMapping("/{recruiterId}/job-fields/{jobFieldId}")
    public ResponseEntity<String> addJobField(@PathVariable UUID recruiterId, @PathVariable UUID jobFieldId) {
        String message = recruiterService.addJobFieldToRecruiter(recruiterId, jobFieldId);
        if (message.equals("The recruiter with that id does not exist") || message.equals("That job field does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("The recruiter already has that job field")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @PatchMapping("/{recruiterId}/company")
    public ResponseEntity<String> changeCompany(@PathVariable UUID recruiterId, @RequestParam int companyCode) {
        String message = recruiterService.changeCompany(recruiterId, companyCode);
        if (message.equals("The recruiter with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("The company with that companyCode does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("That company is taken by another recruiter")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRecruiter(@PathVariable UUID id) {
        String message = recruiterService.deleteRecruiter(id);
        if (message.equals("The recruiter with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("That recruiter has job applications, delete them first")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
