package com.example.job_application_management_system.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.job_application_management_system.entity.Applicant;
import com.example.job_application_management_system.entity.ApplicationStatus;
import com.example.job_application_management_system.entity.JobApplication;
import com.example.job_application_management_system.entity.Recruiter;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {

    List<JobApplication> findByStatusOrderByApplicationDateAsc(ApplicationStatus status);
    List<JobApplication> findByApplicationDateBetweenOrderByApplicationDateAsc(LocalDate start, LocalDate end);

    boolean existsByRecruiterAndApplicationDateAndStatusNot(Recruiter recruiter, LocalDate applicationDate, ApplicationStatus status);

    boolean existsByApplicant(Applicant applicant);
    boolean existsByRecruiter(Recruiter recruiter);

    long countByApplicationDate(LocalDate applicationDate);

    long countByStatus(ApplicationStatus status);
    @Query("SELECT a.status, COUNT(a) FROM JobApplication a GROUP BY a.status")
    List<Object[]> countApplicationsPerStatus();

    @Modifying
    @Query("UPDATE JobApplication a SET a.status = :rejected WHERE a.recruiter.id = :recruiterId AND a.applicationDate = :date AND a.status <> :shortlisted")
    int cancelDay(@Param("recruiterId") UUID recruiterId, @Param("date") LocalDate date,
                  @Param("rejected") ApplicationStatus rejected, @Param("shortlisted") ApplicationStatus shortlisted);

}
