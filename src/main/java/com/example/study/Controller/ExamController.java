package com.example.study.Controller;


import com.example.study.Entity.Exam;
import com.example.study.Entity.User;
import com.example.study.repository.ExamRepository;
import com.example.study.repository.UserRepository;
import com.example.study.repository.User_ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    User_ExamRepository user_ExamRepository;
    @Autowired
    UserRepository  userRepository;
    @Autowired
    ExamRepository examRepository;
    @GetMapping("")
    public String exam(@RequestParam(value = "id", required = false) int id, Model model) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findUserByUsername(authentication.getName());
        Exam ex =examRepository.findExamByID(id);
        model.addAttribute("user_exam", user_ExamRepository.findAllByExamAndUser(ex, user.orElse(null)));
        return "DetailExam/exam_detail";
    }

}
