package com.campusconnect.controller;

import com.campusconnect.entity.Student;
import com.campusconnect.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/profile")
    public ResponseEntity<Student> getProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(studentService.getStudentByUserEmail(email));
    }

    @PutMapping("/profile")
    public ResponseEntity<Student> updateProfile(Authentication authentication, @RequestBody Student student) {
        String email = authentication.getName();
        return ResponseEntity.ok(studentService.updateProfile(email, student));
    }
}
