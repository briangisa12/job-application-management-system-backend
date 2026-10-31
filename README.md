# Job Application Management System

Backend API for managing job applications using Spring Boot, Spring Data JPA and PostgreSQL.

- Student name: Gisagara Ganza Brian
- Student ID: 27267
- Project: job-application-management-system

## How to run

1. Make sure you have Java 21, Maven and PostgreSQL installed.
2. Create the database:
```sql
CREATE DATABASE job_application_management_system;
```
3. Change username/password in `src/main/resources/application.properties` if needed.
4. Run the project:
```
mvn spring-boot:run
```
5. App starts on http://localhost:8080

## Postman

Import the file `job-application-management-system-midterm.postman_collection.json` into Postman.
It contains one request for each requirement (A2, B1, B2, B3, C1-C4, D1-D4, E1-E3, F1, F2, and bonus).

Send the five `A2 - Get All ...` requests first (and `E2` for the interview id), they fill in the id variables.
The delete, status and move requests really change the data, so send them one at a time instead of running the whole collection on real data.

## Main endpoints

CRUD for: job-fields, companies, recruiters, applicants, job-applications

- POST /api/{path}/save
- GET /api/{path}/all
- GET /api/{path}/{id}
- PUT /api/{path}/update/{id}
- DELETE /api/{path}/delete/{id}

Other endpoints:
- GET /api/applicants/by-last-name?lastName=gisagara
- GET /api/job-applications/by-status?status=SUBMITTED
- GET /api/job-applications/between?start=2026-10-01&end=2026-10-31
- GET /api/recruiters/by-job-field?name=software
- GET /api/companies/free
- GET /api/job-fields/unused
- GET /api/applicants/of-recruiter/{recruiterId}
- PATCH /api/job-applications/{id}/status?status=SCREENED
- PUT /api/recruiters/{recruiterId}/job-fields/{jobFieldId}
- PATCH /api/recruiters/{recruiterId}/company?companyCode=1004
- POST /api/interviews/save?jobApplicationId=...
- GET /api/interviews/applicant/{nationalId}
- GET /api/job-applications/stats/by-status
- PATCH /api/job-applications/cancel-day?recruiterId=...&date=2026-10-20
- GET /api/dashboard/summary
- GET /api/job-applications/page?page=0&size=5&sort=applicationDate,desc

Seed data should be entered using the save endpoints.
