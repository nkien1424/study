package com.example.study.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name = "user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true, nullable = false)
    String username;

    @Column(nullable = true)
    String email;
    @Column(nullable = true)
    String provider;
    @Column(nullable = false)
    //ize(min=8,message("Password is 8 charecter at least"))
    String password;
    @Column(nullable = true)
    String providerId;
    @Column(nullable = true)
    String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    Set<String> roles;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserExam> examUsers = new ArrayList<>();


}

