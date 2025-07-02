package com.example.study.repository;

import com.example.study.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUsernameAndPassword(String username, String password);
    User findUserByUsername(String username);

    //User findUserByUsername(String username);
    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    @Override
    boolean existsById(String s);

    User findUserById(Integer id);

}
