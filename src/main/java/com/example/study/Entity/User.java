package com.example.study.Entity;

import com.jayway.jsonpath.internal.function.text.Length;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;




@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
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

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> commentList = new ArrayList<>();
}
