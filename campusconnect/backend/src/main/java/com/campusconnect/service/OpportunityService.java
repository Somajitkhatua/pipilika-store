package com.campusconnect.service;

import com.campusconnect.dto.OpportunityRequest;
import com.campusconnect.entity.Opportunity;
import com.campusconnect.entity.Recruiter;
import com.campusconnect.repository.OpportunityRepository;
import com.campusconnect.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpportunityService {

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    public List<Opportunity> getAllOpportunities() {
        return opportunityRepository.findAll();
    }

    public Opportunity getOpportunityById(Long id) {
        return opportunityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opportunity not found with id: " + id));
    }

    public Opportunity createOpportunity(String recruiterEmail, OpportunityRequest request) {
        Recruiter recruiter = recruiterRepository.findByUser(
            recruiterRepository.findByUser(null).map(r -> r.getUser()).orElse(null) // or direct lookup
        ).orElseGet(() -> {
            // Find recruiter by email through user repository
            return recruiterRepository.findAll().stream()
                .filter(r -> r.getUser().getEmail().equals(recruiterEmail))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        });

        Opportunity opp = new Opportunity();
        opp.setRecruiter(recruiter);
        opp.setTitle(request.getTitle());
        opp.setDescription(request.getDescription());
        opp.setType(request.getType());
        opp.setCompanyName(recruiter.getCompanyName());
        opp.setLocation(request.getLocation());
        opp.setWorkMode(request.getWorkMode());
        opp.setSalary(request.getSalary());
        opp.setSkillsRequired(request.getSkillsRequired());
        opp.setDeadline(request.getDeadline());
        opp.setStatus("ACTIVE");

        return opportunityRepository.save(opp);
    }

    public List<Opportunity> getOpportunitiesByRecruiter(String email) {
        Recruiter recruiter = recruiterRepository.findAll().stream()
                .filter(r -> r.getUser().getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        return opportunityRepository.findByRecruiter(recruiter);
    }

    public List<Opportunity> searchOpportunities(String keyword) {
        return opportunityRepository.searchOpportunities(keyword);
    }

    public List<Opportunity> filterOpportunities(Opportunity.OpportunityType type, Opportunity.WorkMode workMode) {
        return opportunityRepository.filterOpportunities(type, workMode);
    }

    public void deleteOpportunity(Long id) {
        opportunityRepository.deleteById(id);
    }
}
