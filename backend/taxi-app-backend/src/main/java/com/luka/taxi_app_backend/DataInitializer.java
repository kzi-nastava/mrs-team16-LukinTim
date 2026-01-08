package com.luka.taxi_app_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.luka.taxi_app_backend.model.User;
import com.luka.taxi_app_backend.model.UserRoleEnum;
import com.luka.taxi_app_backend.repository.UserRepository;

@Configuration
public class DataInitializer {

  @Bean
  CommandLineRunner initUsers(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    return args -> {

      String email = "lukanikolic98@hotmail.com";

      if (userRepository.findByEmail(email).isPresent()) {
        return; // user already exists → do nothing
      }

      User user = new User();
      user.setEmail(email);
      user.setFirstname("Luka");
      user.setLastname("Nikolic");
      user.setPassword(passwordEncoder.encode("Invalid4ever")); // 👈 known password
      user.setRole(UserRoleEnum.ROLE_REGULAR);
      user.setActivated(true);
      user.setAddress("Adress1");
      user.setPhonenumber("0657435435");

      userRepository.save(user);

      System.out.println("✔ Default user created: " + email);
    };
  }
}
