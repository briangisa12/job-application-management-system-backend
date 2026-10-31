package com.example.job_application_management_system.controller;

import com.example.job_application_management_system.entity.JobField;
import com.example.job_application_management_system.service.JobFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-fields")
public class JobFieldController {

    @Autowired
    private JobFieldService jobFieldService;

    @PostMapping("/save")
    public ResponseEntity<String> saveJobField(@RequestBody JobField jobField) {
        String message = jobFieldService.saveJobField(jobField);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobField>> getAllJobFields() {
        List<JobField> jobFields = jobFieldService.getAllJobFields();
        return new ResponseEntity<>(jobFields, HttpStatus.OK);
    }

    // job fields that nobody holds
    @GetMapping("/unused")
    public ResponseEntity<List<JobField>> getUnusedJobFields() {
        return new ResponseEntity<>(jobFieldService.getUnusedJobFields(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobFieldById(@PathVariable UUID id) {
        Optional<JobField> jobField = jobFieldService.getJobFieldById(id);
        if (jobField.isPresent()) {
            return new ResponseEntity<>(jobField.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The job field with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateJobField(@PathVariable UUID id, @RequestBody JobField jobField) {
        String message = jobFieldService.updateJobField(id, jobField);
        if (message.equals("The job field with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteJobField(@PathVariable UUID id) {
        String message = jobFieldService.deleteJobField(id);
        if (message.equals("The job field with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("That job field is held by a recruiter")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
