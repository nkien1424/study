package com.example.study.Entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "exam")
public class Exam {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int ID;

  @Column(unique = true, nullable = false)
  private String name;

  @Column(unique = false, nullable = true)
  private String description;

  @Column(unique = false, nullable = true)
  private String year;

  @Column(unique = false, nullable = true)
  private Date create_At;

  @ElementCollection
  @CollectionTable(
      name = "exam_questions", // Tên bảng collection
      joinColumns = @JoinColumn(name = "exam_id") // Khóa ngoại
      )
  @Column(name = "question_id") // Tên cột lưu nội dung câu hỏi
  private List<String> questions = new ArrayList<>();

  @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserExam> examUsers = new ArrayList<>();

  @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> commentList = new ArrayList<>();
}
