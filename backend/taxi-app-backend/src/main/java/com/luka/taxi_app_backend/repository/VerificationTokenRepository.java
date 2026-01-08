package com.luka.taxi_app_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.luka.taxi_app_backend.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
  Optional<VerificationToken> findByToken(String token);
}