package com.campusconnect.controller;

import com.campusconnect.entity.Recruiter;
import com.campusconnect.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiters")
@CrossOrigin(origins = "*")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    @GetMapping("/profile")
    public ResponseEntity<Recruiter> getProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(recruiterService.getRecruiterByUserEmail(email));
    }

    @PutMapping("/profile")
    public ResponseEntity<Recruiter> updateProfile(Authentication authentication, @RequestBody Recruiter recruiter) {
        String email = authentication.getName();
        return ResponseEntity.ok(recruiterService.updateProfile(email, recruiter));
    }
}
