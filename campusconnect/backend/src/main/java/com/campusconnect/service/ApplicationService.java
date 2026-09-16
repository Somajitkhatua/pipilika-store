package com.campusconnect.service;

import com.campusconnect.dto.ApplicationRequest;
import com.campusconnect.entity.Application;
import com.campusconnect.entity.Opportunity;
import com.campusconnect.entity.Recruiter;
import com.campusconnect.entity.Student;
import com.campusconnect.entity.User;
import com.campusconnect.repository.ApplicationRepository;
import com.campusconnect.repository.OpportunityRepository;
import com.campusconnect.repository.RecruiterRepository;
import com.campusconnect.repository.StudentRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private UserRepository userRepository;

    public Application applyForOpportunity(String studentEmail, Long opportunityId, ApplicationRequest request) {
        User user = userRepository.findByEmail(studentEmail).orElseThrow(() -> new RuntimeException("User not found"));
        Student student = studentRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Student not found"));

        Opportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));

        if (applicationRepository.findByStudentAndOpportunity(student, opportunity).isPresent()) {
            throw new RuntimeException("You have already applied for this opportunity!");
        }

        Application application = new Application();
        application.setStudent(student);
        application.setOpportunity(opportunity);
        application.setResumeUrl(request.getResumeUrl() != null ? request.getResumeUrl() : student.getResumeUrl());
        application.setCoverLetter(request.getCoverLetter());
        application.setStatus(Application.ApplicationStatus.APPLIED);

        return applicationRepository.save(application);
    }

    public List<Application> getApplicationsByStudent(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Student student = studentRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Student not found"));
        return applicationRepository.findByStudent(student);
    }

    public List<Application> getApplicationsForRecruiter(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Recruiter recruiter = recruiterRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Recruiter not found"));
        return applicationRepository.findByOpportunityRecruiterId(recruiter.getId());
    }

    public List<Application> getApplicationsByOpportunity(Long opportunityId) {
        Opportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));
        return applicationRepository.findByOpportunity(opportunity);
    }

    public Application updateApplicationStatus(Long applicationId, Application.ApplicationStatus status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(status);
        application.setUpdatedAt(LocalDateTime.now());
        return applicationRepository.save(application);
    }
}
