package com.campusconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeMatchResponse {
    private int matchScore;
    private List<String> matchingSkills;
    private List<String> missingSkills;
    private List<String> studentSkills;
    private List<String> requiredSkills;
}
