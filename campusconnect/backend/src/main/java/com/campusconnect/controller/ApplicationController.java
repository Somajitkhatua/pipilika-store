package com.campusconnect.controller;

import com.campusconnect.dto.ApplicationRequest;
import com.campusconnect.entity.Application;
import com.campusconnect.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/{opportunityId}")
    public ResponseEntity<Application> applyForOpportunity(
            Authentication authentication,
            @PathVariable Long opportunityId,
            @RequestBody ApplicationRequest request) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.applyForOpportunity(email, opportunityId, request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Application>> getMyApplications(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.getApplicationsByStudent(email));
    }

    @GetMapping("/recruiter")
    public ResponseEntity<List<Application>> getRecruiterApplications(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.getApplicationsForRecruiter(email));
    }

    @GetMapping("/opportunity/{opportunityId}")
    public ResponseEntity<List<Application>> getApplicationsByOpportunity(@PathVariable Long opportunityId) {
        return ResponseEntity.ok(applicationService.getApplicationsByOpportunity(opportunityId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Application> updateStatus(
            @PathVariable Long id,
            @RequestParam Application.ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(id, status));
    }
}
