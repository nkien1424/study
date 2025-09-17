package com.example.study.Entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "question")
public class Question {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int ID;

  @Column(unique = true, nullable = false)
  private String question;

  @Column(unique = false, nullable = true)
  private String audio;

  @Column(nullable = false)
  private String correctAnswer;

  @ElementCollection
  @CollectionTable(name = "question_answers", joinColumns = @JoinColumn(name = "question_id"))
  @Column(name = "answer")
  private List<String> answer;

  @Column(unique = false, nullable = false)
  private int part;

  @Column(unique = false, nullable = true)
  private String Image;

  @Column(unique = false, nullable = true)
  private String optionA;

  @Column(unique = false, nullable = false)
  private int number;

  @Column(unique = false, nullable = true)
  private String optionB;

  @Column(unique = false, nullable = true)
  private String optionC;

  @Column(unique = false, nullable = true)
  private String optionD;

  @Column(unique = false, nullable = true)
  private int question_group;

  @ManyToOne
  @JoinColumn(name = "exam_id", nullable = false)
  private Exam exam;
}
