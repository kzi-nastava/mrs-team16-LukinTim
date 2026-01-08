package com.luka.taxi_app_backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
  private String email;
  private String password;
  private String firstname;
  private String lastname;
  private String address;
  private String phonenumber;
}
