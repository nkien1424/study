package com.example.study.Controller;


import com.example.study.Entity.Exam;
import com.example.study.Entity.PracticeRequest;
import com.example.study.Entity.User;
import com.example.study.repository.ExamRepository;
import com.example.study.repository.QuestionRepository;
import com.example.study.repository.UserRepository;
import com.example.study.repository.User_ExamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    User_ExamRepository user_ExamRepository;
    @Autowired
    UserRepository  userRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ExamRepository examRepository;
    Integer id;
    PracticeRequest practiceRequest;
    @GetMapping("")
    public String exam(@RequestParam(value = "id_exam", required = false) int id_exam,@RequestParam(value = "id_user", required = false) String id_user,  Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        log.info("Principal: {}", auth.getPrincipal());
//        auth.getAuthorities().forEach(authority -> {
//            log.info("Authority: {}", authority.getAuthority()); // Quan trọng: xem giá trị thực tế
//        });
        Exam ex =examRepository.findExamByID(id_exam);
        model.addAttribute("exam",ex);
        if(id_user != null) {
            User user = userRepository.findUserById(Integer.valueOf(id_user));

            model.addAttribute("user_exam", user_ExamRepository.findAllByExamAndUser(ex, user));
        }

        return "DetailExam/exam_detail";
    }
    @GetMapping("/test")
    public String test(Model model) {

        model.addAttribute("questions",questionRepository.findAllByExam_ID(id));
        model.addAttribute("time",practiceRequest.getTimeLimit());
        return "Test/practice";
    }

    @PostMapping("/practice")
    public String practice(@RequestBody PracticeRequest request, RedirectAttributes redirectAttributes) {
        practiceRequest=request;

        return "redirect:/exam/test";
    }
}
