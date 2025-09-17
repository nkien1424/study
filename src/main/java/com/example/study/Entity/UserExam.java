package com.example.study.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "exam_user")
public class UserExam {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "exam_id", nullable = false)
  private Exam exam;

  @Column(name = "name_exam")
  private String name_exam; //

  @Column(name = "score")
  private String score; // Điểm số

  @Column(name = "completed_at")
  private LocalDateTime completedAt; // Thời gian hoàn thành

  // Getters and Setters (đã có @Getter/@Setter)
}
