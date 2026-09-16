package com.campusconnect.repository;

import com.campusconnect.entity.Recruiter;
import com.campusconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    Optional<Recruiter> findByUser(User user);
    Optional<Recruiter> findByUserId(Long userId);
}
