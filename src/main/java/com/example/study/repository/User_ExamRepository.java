package com.example.study.repository;


import com.example.study.Entity.Exam;
import com.example.study.Entity.User;
import com.example.study.Entity.UserExam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface User_ExamRepository extends JpaRepository<UserExam,String> {
    List<UserExam> findAllByExamAndUser(Exam exam, User user);
    
}
