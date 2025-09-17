package com.example.study.repository;

import com.example.study.Entity.Comment;
import com.example.study.Entity.Exam;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
  List<Comment> findByExam(Exam exam);
}
