package com.campusconnect.service;

import com.campusconnect.entity.Resume;
import com.campusconnect.entity.Student;
import com.campusconnect.entity.User;
import com.campusconnect.repository.ResumeRepository;
import com.campusconnect.repository.StudentRepository;
import com.campusconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    private final String uploadDir = "uploads/resumes/";

    public Resume uploadResume(String studentEmail, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(studentEmail).orElseThrow(() -> new RuntimeException("User not found"));
        Student student = studentRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Student not found"));

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String fileUrl = "/uploads/resumes/" + fileName;

        Resume resume = new Resume();
        resume.setStudent(student);
        resume.setFileName(file.getOriginalFilename());
        resume.setFileUrl(fileUrl);
        Resume savedResume = resumeRepository.save(resume);

        // Update student's default resume URL
        student.setResumeUrl(fileUrl);
        studentRepository.save(student);

        return savedResume;
    }

    public List<Resume> getResumesByStudent(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Student student = studentRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Student not found"));
        return resumeRepository.findByStudent(student);
    }
}
