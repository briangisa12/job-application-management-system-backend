package com.example.job_application_management_system.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.job_application_management_system.entity.Company;
import com.example.job_application_management_system.entity.JobField;
import com.example.job_application_management_system.entity.Recruiter;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, UUID> {

    @Query("SELECT DISTINCT r FROM Recruiter r JOIN r.jobFields j WHERE LOWER(j.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Recruiter> findRecruitersByJobField(@Param("name") String name);
    boolean existsByCompany(Company company);

    boolean existsByJobFields(JobField jobField);

}
