package com.campusconnect.repository;

import com.campusconnect.entity.Opportunity;
import com.campusconnect.entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
    List<Opportunity> findByRecruiter(Recruiter recruiter);
    List<Opportunity> findByStatus(String status);
    
    @Query("SELECT o FROM Opportunity o WHERE LOWER(o.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(o.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(o.skillsRequired) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Opportunity> searchOpportunities(@Param("keyword") String keyword);

    @Query("SELECT o FROM Opportunity o WHERE (:type IS NULL OR o.type = :type) AND (:workMode IS NULL OR o.workMode = :workMode)")
    List<Opportunity> filterOpportunities(@Param("type") Opportunity.OpportunityType type, @Param("workMode") Opportunity.WorkMode workMode);
}
