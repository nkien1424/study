package com.example.study.repository;

import com.example.study.Entity.Vocabulary;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Integer> {
  List<Vocabulary> findByTopicId(Long topicId);

  List<Vocabulary> findByEnglishWordContainingIgnoreCase(String keyword);

  List<Vocabulary> findByTopicIdAndEnglishWordContainingIgnoreCase(Long topicId, String keyword);
}
