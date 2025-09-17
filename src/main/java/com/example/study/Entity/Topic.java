package com.example.study.Entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "topic")
public class Topic {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private String description;

  @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
  private List<Vocabulary> vocabularies;

  // getters, setters, constructors
}
