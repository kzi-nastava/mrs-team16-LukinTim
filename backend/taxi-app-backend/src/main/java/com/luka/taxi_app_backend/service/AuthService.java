package com.luka.taxi_app_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.luka.taxi_app_backend.dto.RegisterRequest;
import com.luka.taxi_app_backend.model.User;
import com.luka.taxi_app_backend.model.UserRoleEnum;
import com.luka.taxi_app_backend.model.VerificationToken;
import com.luka.taxi_app_backend.repository.UserRepository;
import com.luka.taxi_app_backend.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private VerificationTokenRepository tokenRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private EmailService emailService;

  public VerificationToken registerRegularUser(RegisterRequest request) {

    System.out.println(request.toString());

    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new RuntimeException("Email already taken");
    }

    User user = new User();
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setFirstname(request.getFirstname());
    user.setLastname(request.getLastname());
    user.setAddress(request.getAddress());
    user.setPhonenumber(request.getPhonenumber());
    user.setRole(UserRoleEnum.ROLE_REGULAR);

    user.setActivated(false);

    userRepository.save(user);

    VerificationToken token = new VerificationToken();
    token.setToken(UUID.randomUUID().toString());
    token.setUser(user);
    token.setExpiryDate(LocalDateTime.now().plusHours(24));
    token.setUsed(false);

    tokenRepository.save(token);

    String verificationLink = "http://localhost:8080/auth/confirm-registration?token=" + token.getToken();
    emailService.sendEmail(user.getEmail(), "Verify your account",
        "Click the link to activate your account: " + verificationLink);

    return token;
  }

  public void verifyToken(String tokenStr) {
    VerificationToken token = tokenRepository.findByToken(tokenStr)
        .orElseThrow(() -> new RuntimeException("Invalid token"));

    if (token.isUsed()) {
      throw new RuntimeException("Token already used, account already activated");
    }

    if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("Token expired");
    }

    token.setUsed(true);
    tokenRepository.save(token);

    User user = token.getUser();
    user.setActivated(true);
    userRepository.save(user);

    return;
  }
}
