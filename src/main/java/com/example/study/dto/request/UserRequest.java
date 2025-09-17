package com.example.study.dto.request;

import lombok.Data;

@Data
public class UserRequest {
  private String username;
  private String password;
  private String re_password;
}
