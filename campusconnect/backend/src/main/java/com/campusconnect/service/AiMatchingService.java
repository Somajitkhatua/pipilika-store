package com.campusconnect.service;

import com.campusconnect.dto.ResumeMatchResponse;
import com.campusconnect.entity.Opportunity;
import com.campusconnect.entity.Student;
import com.campusconnect.entity.User;
import com.campusconnect.repository.OpportunityRepository;
import com.campusconnect.repository.StudentRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiMatchingService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private UserRepository userRepository;

    public ResumeMatchResponse matchStudentWithOpportunity(String studentEmail, Long opportunityId) {
        User user = userRepository.findByEmail(studentEmail).orElseThrow(() -> new RuntimeException("User not found"));
        Student student = studentRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Student not found"));
        Opportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));

        List<String> studentSkills = parseSkills(student.getSkills());
        List<String> requiredSkills = parseSkills(opportunity.getSkillsRequired());

        List<String> matchingSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        if (requiredSkills.isEmpty()) {
            return new ResumeMatchResponse(100, studentSkills, new ArrayList<>(), studentSkills, requiredSkills);
        }

        for (String req : requiredSkills) {
            boolean found = studentSkills.stream()
                    .anyMatch(s -> s.equalsIgnoreCase(req) || s.contains(req) || req.contains(s));
            if (found) {
                matchingSkills.add(req);
            } else {
                missingSkills.add(req);
            }
        }

        int matchScore = (int) Math.round(((double) matchingSkills.size() / requiredSkills.size()) * 100);

        return new ResumeMatchResponse(matchScore, matchingSkills, missingSkills, studentSkills, requiredSkills);
    }

    private List<String> parseSkills(String skillsStr) {
        if (skillsStr == null || skillsStr.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(skillsStr.split("[,\\-\\/|\\n]"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
