package com.example.study.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "comment")
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false) // KHÓA NGOẠI tới bảng user
  private User user;

  @ManyToOne
  @JoinColumn(name = "exam_id", nullable = false) // KHÓA NGOẠI tới bảng exam
  private Exam exam;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "avatar", nullable = true)
  private String avatar;

  @Column(name = "time", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime time;

  // getters, setters
}
