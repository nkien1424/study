package com.example.study.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vocabulary")
public class Vocabulary {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "topic_id", nullable = false)
  private Topic topic;

  @Column(name = "english_word", nullable = false)
  private String englishWord;

  @Column(name = "vietnamese_meaning", nullable = false)
  private String vietnameseMeaning;

  private String pronunciation;

  @Column(name = "example_sentence")
  private String exampleSentence;

  // getters, setters, constructors
}
