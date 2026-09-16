package com.campusconnect.repository;

import com.campusconnect.entity.Application;
import com.campusconnect.entity.Opportunity;
import com.campusconnect.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudent(Student student);
    List<Application> findByOpportunity(Opportunity opportunity);
    List<Application> findByOpportunityRecruiterId(Long recruiterId);
    Optional<Application> findByStudentAndOpportunity(Student student, Opportunity opportunity);
}
