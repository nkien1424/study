package com.example.study.configuration;

import com.example.study.Entity.User;
import com.example.study.enums.Role;
import com.example.study.repository.UserRepository;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationInitConfig {
  @Autowired PasswordEncoder passwordEncoder;

  @Bean
  ApplicationRunner applicationRunner(UserRepository userRepository) {
    return args -> {
      if (userRepository.findUserByUsername("admin") == null) {
        var roles = new HashSet<String>();
        roles.add(Role.ADMIN.name());
        User user =
            User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .name("Trinh Ngoc Kien")
                .roles(roles)
                .build();
        userRepository.save(user);
      }
    };
  }
}
