package com.example.study.repository;

import com.example.study.Entity.Exam;
import com.example.study.Entity.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
  List<Question> findAllByPart(int part);

  List<Question> findAllByExam_ID(Integer exam_id);

  Question findAllByNumberAndPartAndExam(int number, int part, Exam exam);
}
