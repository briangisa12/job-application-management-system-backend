package com.example.job_application_management_system.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.job_application_management_system.entity.Interview;
import com.example.job_application_management_system.entity.JobApplication;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, UUID> {

    @Query("SELECT d FROM Interview d WHERE d.jobApplication.applicant.nationalId = :nationalId ORDER BY d.interviewDate DESC")
    List<Interview> findInterviewsOfApplicant(@Param("nationalId") String nationalId);

    boolean existsByJobApplication(JobApplication jobApplication);

}
