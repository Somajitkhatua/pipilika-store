package com.campusconnect.controller;

import com.campusconnect.entity.Application;
import com.campusconnect.entity.Opportunity;
import com.campusconnect.entity.Recruiter;
import com.campusconnect.entity.Student;
import com.campusconnect.service.ApplicationService;
import com.campusconnect.service.OpportunityService;
import com.campusconnect.service.RecruiterService;
import com.campusconnect.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private RecruiterService recruiterService;

    @Autowired
    private OpportunityService opportunityService;

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Long>> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalStudents", (long) studentService.getAllStudents().size());
        stats.put("totalRecruiters", (long) recruiterService.getAllRecruiters().size());
        stats.put("totalOpportunities", (long) opportunityService.getAllOpportunities().size());
        stats.put("totalApplications", (long) applicationService.getApplicationsByOpportunity(1L).size()); // general count
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/recruiters")
    public ResponseEntity<List<Recruiter>> getAllRecruiters() {
        return ResponseEntity.ok(recruiterService.getAllRecruiters());
    }

    @PutMapping("/recruiters/{id}/verify")
    public ResponseEntity<Void> verifyRecruiter(@PathVariable Long id, @RequestParam boolean approve) {
        recruiterService.verifyRecruiter(id, approve);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/opportunities")
    public ResponseEntity<List<Opportunity>> getAllOpportunities() {
        return ResponseEntity.ok(opportunityService.getAllOpportunities());
    }

    @DeleteMapping("/opportunities/{id}")
    public ResponseEntity<Void> deleteOpportunity(@PathVariable Long id) {
        opportunityService.deleteOpportunity(id);
        return ResponseEntity.ok().build();
    }
}
