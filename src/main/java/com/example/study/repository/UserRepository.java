package com.example.study.repository;

import com.example.study.Entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findUserByUsernameAndPassword(String username, String password);

  User findUserByUsername(String username);

  // User findUserByUsername(String username);
  Optional<User> findByProviderAndProviderId(String provider, String providerId);

  @Override
  boolean existsById(String s);

  User findUserById(Integer id);
}
