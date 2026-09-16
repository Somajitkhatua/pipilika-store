package com.campusconnect.service;

import com.campusconnect.entity.Student;
import com.campusconnect.entity.User;
import com.campusconnect.repository.StudentRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    public Student getStudentByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
    }

    public Student updateProfile(String email, Student updatedStudent) {
        Student student = getStudentByUserEmail(email);
        
        if (updatedStudent.getCollege() != null) student.setCollege(updatedStudent.getCollege());
        if (updatedStudent.getDegree() != null) student.setDegree(updatedStudent.getDegree());
        if (updatedStudent.getBranch() != null) student.setBranch(updatedStudent.getBranch());
        if (updatedStudent.getGraduationYear() != null) student.setGraduationYear(updatedStudent.getGraduationYear());
        if (updatedStudent.getCgpa() != null) student.setCgpa(updatedStudent.getCgpa());
        if (updatedStudent.getSkills() != null) student.setSkills(updatedStudent.getSkills());
        if (updatedStudent.getPhone() != null) student.setPhone(updatedStudent.getPhone());
        if (updatedStudent.getLocation() != null) student.setLocation(updatedStudent.getLocation());
        if (updatedStudent.getGithubUrl() != null) student.setGithubUrl(updatedStudent.getGithubUrl());
        if (updatedStudent.getLinkedinUrl() != null) student.setLinkedinUrl(updatedStudent.getLinkedinUrl());
        if (updatedStudent.getResumeUrl() != null) student.setResumeUrl(updatedStudent.getResumeUrl());

        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
