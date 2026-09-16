package com.campusconnect.dto;

import com.campusconnect.entity.Opportunity;
import lombok.Data;
import java.time.LocalDate;

@Data
public class OpportunityRequest {
    private String title;
    private String description;
    private Opportunity.OpportunityType type;
    private String companyName;
    private String location;
    private Opportunity.WorkMode workMode;
    private String salary;
    private String skillsRequired;
    private LocalDate deadline;
}
