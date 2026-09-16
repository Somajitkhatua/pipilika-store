package com.campusconnect.service;

import com.campusconnect.entity.Recruiter;
import com.campusconnect.entity.User;
import com.campusconnect.repository.RecruiterRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private UserRepository userRepository;

    public Recruiter getRecruiterByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return recruiterRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
    }

    public Recruiter updateProfile(String email, Recruiter updated) {
        Recruiter recruiter = getRecruiterByUserEmail(email);
        if (updated.getCompanyName() != null) recruiter.setCompanyName(updated.getCompanyName());
        if (updated.getCompanyDescription() != null) recruiter.setCompanyDescription(updated.getCompanyDescription());
        if (updated.getWebsite() != null) recruiter.setWebsite(updated.getWebsite());
        if (updated.getLocation() != null) recruiter.setLocation(updated.getLocation());
        if (updated.getIndustry() != null) recruiter.setIndustry(updated.getIndustry());
        return recruiterRepository.save(recruiter);
    }

    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }

    public void verifyRecruiter(Long id, boolean approve) {
        Recruiter recruiter = recruiterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        recruiter.setVerified(approve);
        recruiterRepository.save(recruiter);
    }
}
