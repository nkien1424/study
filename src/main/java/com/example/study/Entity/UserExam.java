package com.example.study.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "exam_user")
public class UserExam {
    @Id
    private String id;  // Composite key (user_id + exam_id)

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("examId")
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Column(name = "name_exam")
    private String name_exam;  //
    @Column(name = "score")
    private String score;  // Điểm số

    @Column(name = "completed_at")
    private LocalDateTime completedAt;  // Thời gian hoàn thành

    // Getters and Setters (đã có @Getter/@Setter)
}
