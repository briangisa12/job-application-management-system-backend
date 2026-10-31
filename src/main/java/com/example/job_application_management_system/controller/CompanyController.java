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
import org.springframework.web.bind.annotation.RestController;

import com.example.job_application_management_system.entity.Company;
import com.example.job_application_management_system.service.CompanyService;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/save")
    public ResponseEntity<String> saveCompany(@RequestBody Company company) {
        String message = companyService.saveCompany(company);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return new ResponseEntity<>(companyService.getAllCompanies(), HttpStatus.OK);
    }
    @GetMapping("/free")
    public ResponseEntity<List<Company>> getFreeCompanies() {
       List<Company> companies = companyService.getFreeCompanies();
     return new ResponseEntity<>(companies, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable UUID id) {
  Optional<Company> company = companyService.getCompanyById(id);
      if (company.isPresent()) {
             return new ResponseEntity<>(company.get(), HttpStatus.OK);
        } else {
    return new ResponseEntity<>("The company with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCompany(@PathVariable UUID id, @RequestBody Company company) {
        String message = companyService.updateCompany(id, company);
        if (message.equals("The company with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable UUID id) {
        String message = companyService.deleteCompany(id);
        if (message.equals("The company with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("That company has a recruiter, move the recruiter first")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
