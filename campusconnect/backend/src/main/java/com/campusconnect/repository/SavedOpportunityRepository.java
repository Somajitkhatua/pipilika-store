package com.campusconnect.repository;

import com.campusconnect.entity.Opportunity;
import com.campusconnect.entity.SavedOpportunity;
import com.campusconnect.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavedOpportunityRepository extends JpaRepository<SavedOpportunity, Long> {
    List<SavedOpportunity> findByStudent(Student student);
    Optional<SavedOpportunity> findByStudentAndOpportunity(Student student, Opportunity opportunity);
    void deleteByStudentAndOpportunity(Student student, Opportunity opportunity);
}
