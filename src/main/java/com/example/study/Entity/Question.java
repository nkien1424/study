package com.example.study.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
//    @Column(unique = false, nullable = true)
//    private String Image;
    @ManyToOne
    @JoinColumn(name = "exam_id",nullable = false)
    @CollectionTable(name = "exam_id", joinColumns = @JoinColumn(name = "user_id"))
    private Exam exam;
}
