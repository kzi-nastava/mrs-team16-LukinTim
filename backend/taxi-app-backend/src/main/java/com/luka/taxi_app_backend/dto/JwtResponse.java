package com.luka.taxi_app_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.luka.taxi_app_backend.model.UserRoleEnum;

@Data
@AllArgsConstructor
public class JwtResponse {
  private String accessToken;
  private String refreshToken;
  private String email;
  private String firstname;
  private String lastname;
  private UserRoleEnum role;
}