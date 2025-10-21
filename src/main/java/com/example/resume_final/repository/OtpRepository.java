package com.example.resume_final.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.resume_final.entity.OtpVerification;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findByEmail(String email);
}