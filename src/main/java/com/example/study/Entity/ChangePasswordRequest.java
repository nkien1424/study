package com.example.study.Entity;

import lombok.Data;

@Data
public class ChangePasswordRequest {
  private String oldPassword;

  private String newPassword;

  // getters and setters
}
