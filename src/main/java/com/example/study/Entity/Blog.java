package com.example.study.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "blog")
public class Blog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;


  @Column(unique = false,nullable = true)
  private String image;
  @Column(unique = true, nullable = false)
  private String title;

  @Column(unique = false, nullable = true)
  private String description;

  @Column(unique = true, nullable = false)
  private String author;

  @Column(unique = false, nullable = false)
  private LocalDateTime post_time;

  @Column(unique = false, nullable = false)
  private int type;
}
