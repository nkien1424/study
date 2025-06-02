package com.example.study.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AuthenticationRequest {
    String username;
    String password;

}
