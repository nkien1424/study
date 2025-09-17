package com.example.study.Controller;

import com.example.study.repository.ExamRepository;
import com.example.study.repository.UserRepository;
import com.example.study.repository.User_ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
  @Autowired UserRepository userRepository;
  @Autowired ExamRepository examRepository;
  @Autowired User_ExamRepository user_examRepository;

  @GetMapping("")
  public String index(Model model) {
    model.addAttribute("users", userRepository.findAll());
    model.addAttribute("exams", examRepository.findAll());
    model.addAttribute("user_total", userRepository.findAll().size());
    model.addAttribute("exam_total", examRepository.findAll().size());

    return "admin/admin_dashboard";
  }
}
