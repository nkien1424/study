package com.example.study.dto.response;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Integer id;
    String username;
    //ize(min=8,message("Password is 8 charecter at least"))
    String password;
    String name;
    Set<String> roles;
}

