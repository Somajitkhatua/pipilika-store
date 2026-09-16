package com.campusconnect.dto;

import com.campusconnect.entity.User;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private User.Role role;
    
    // Additional fields for student/recruiter registration if needed
    private String college;
    private String companyName;
}
