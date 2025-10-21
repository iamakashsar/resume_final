package com.example.resume_final.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.resume_final.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}