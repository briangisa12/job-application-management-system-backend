package com.example.job_application_management_system.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.job_application_management_system.entity.JobField;

@Repository
public interface JobFieldRepository extends JpaRepository<JobField, UUID> {

    @Query("SELECT j FROM JobField j WHERE j.recruiters IS EMPTY")
    List<JobField> findUnusedJobFields();

}
