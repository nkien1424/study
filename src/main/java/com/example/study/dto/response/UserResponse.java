package com.example.study.dto.response;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
  Integer id;
  String username;
  String password;
  String name;
  Set<String> roles;
}
