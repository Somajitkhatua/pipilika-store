package com.campusconnect.controller;

import com.campusconnect.dto.ResumeMatchResponse;
import com.campusconnect.entity.Resume;
import com.campusconnect.service.AiMatchingService;
import com.campusconnect.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "*")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private AiMatchingService aiMatchingService;

    @PostMapping("/upload")
    public ResponseEntity<Resume> uploadResume(
            Authentication authentication,
            @RequestParam("file") MultipartFile file) throws IOException {
        String email = authentication.getName();
        return ResponseEntity.ok(resumeService.uploadResume(email, file));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Resume>> getMyResumes(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(resumeService.getResumesByStudent(email));
    }

    @GetMapping("/match/{opportunityId}")
    public ResponseEntity<ResumeMatchResponse> matchResume(
            Authentication authentication,
            @PathVariable Long opportunityId) {
        String email = authentication.getName();
        return ResponseEntity.ok(aiMatchingService.matchStudentWithOpportunity(email, opportunityId));
    }
}
