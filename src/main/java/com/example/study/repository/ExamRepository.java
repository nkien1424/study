package com.example.study.repository;

import com.example.study.Entity.Exam;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
  @Override
  List<Exam> findAll(Sort sort);

  Exam findExamByID(int idexam);
}
