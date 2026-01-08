package com.luka.taxi_app_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.luka.taxi_app_backend.model.UserRoleEnum;

@Data
@AllArgsConstructor
public class LoginResponse {
  private String accessToken;
  private String email;
  private String firstname;
  private String lastname;
  private UserRoleEnum role;
}
