package com.campusconnect.service;

import com.campusconnect.dto.AuthResponse;
import com.campusconnect.dto.LoginRequest;
import com.campusconnect.dto.RegisterRequest;
import com.campusconnect.entity.Recruiter;
import com.campusconnect.entity.Student;
import com.campusconnect.entity.User;
import com.campusconnect.repository.RecruiterRepository;
import com.campusconnect.repository.StudentRepository;
import com.campusconnect.repository.UserRepository;
import com.campusconnect.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setStatus(request.getRole() == User.Role.RECRUITER ? "PENDING" : "ACTIVE");

        User savedUser = userRepository.save(user);

        if (request.getRole() == User.Role.STUDENT) {
            Student student = new Student();
            student.setUser(savedUser);
            student.setCollege(request.getCollege() != null ? request.getCollege() : "University College");
            studentRepository.save(student);
        } else if (request.getRole() == User.Role.RECRUITER) {
            Recruiter recruiter = new Recruiter();
            recruiter.setUser(savedUser);
            recruiter.setCompanyName(request.getCompanyName() != null ? request.getCompanyName() : savedUser.getName() + " Corp");
            recruiter.setVerified(false);
            recruiterRepository.save(recruiter);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, savedUser.getRole().name(), savedUser.getName(), savedUser.getEmail(), savedUser.getId());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, user.getRole().name(), user.getName(), user.getEmail(), user.getId());
    }
}
