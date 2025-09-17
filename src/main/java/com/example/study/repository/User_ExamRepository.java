package com.example.study.repository;

import com.example.study.Entity.Exam;
import com.example.study.Entity.User;
import com.example.study.Entity.UserExam;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User_ExamRepository extends JpaRepository<UserExam, String> {
  List<UserExam> findAllByExamAndUser(Exam exam, User user);

  List<UserExam> findAllByUserId(Integer userId);
}
