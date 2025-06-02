package com.example.study.repository;

import com.example.study.Entity.Exam;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
    @Override
    List<Exam> findAll(Sort sort);

    Exam findExamByID(int idexam);
}
