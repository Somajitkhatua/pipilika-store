package com.campusconnect.controller;

import com.campusconnect.dto.OpportunityRequest;
import com.campusconnect.entity.Opportunity;
import com.campusconnect.service.OpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
@CrossOrigin(origins = "*")
public class OpportunityController {

    @Autowired
    private OpportunityService opportunityService;

    @GetMapping
    public ResponseEntity<List<Opportunity>> getAllOpportunities() {
        return ResponseEntity.ok(opportunityService.getAllOpportunities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Opportunity> getOpportunityById(@PathVariable Long id) {
        return ResponseEntity.ok(opportunityService.getOpportunityById(id));
    }

    @PostMapping
    public ResponseEntity<Opportunity> createOpportunity(Authentication authentication, @RequestBody OpportunityRequest request) {
        String email = authentication.getName();
        return ResponseEntity.ok(opportunityService.createOpportunity(email, request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Opportunity>> getMyOpportunities(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(opportunityService.getOpportunitiesByRecruiter(email));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Opportunity>> searchOpportunities(@RequestParam String keyword) {
        return ResponseEntity.ok(opportunityService.searchOpportunities(keyword));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Opportunity>> filterOpportunities(
            @RequestParam(required = false) Opportunity.OpportunityType type,
            @RequestParam(required = false) Opportunity.WorkMode workMode) {
        return ResponseEntity.ok(opportunityService.filterOpportunities(type, workMode));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOpportunity(@PathVariable Long id) {
        opportunityService.deleteOpportunity(id);
        return ResponseEntity.ok().build();
    }
}
