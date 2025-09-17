package com.example.study.Controller;

import com.example.study.Entity.User;
import com.example.study.Service.AuthenticationService;
import com.example.study.Service.UserService;
import com.example.study.dto.request.AuthenticationRequest;
import com.example.study.repository.BlogRepository;
import com.example.study.repository.ExamRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("")
public class HomeController {
  private static final Logger log = LoggerFactory.getLogger(HomeController.class);
  @Autowired UserService userService;
  @Autowired AuthenticationService authenticationService;
  @Autowired ExamRepository examRepository;
  @Autowired
  BlogRepository blogRepository;
  @GetMapping("")
  public String home(Model model) {
    model.addAttribute("exams", examRepository.findAll());
    model.addAttribute("blogs", blogRepository.findAll());
    return "home/broadcast";
  }
  @GetMapping("/home")
  public String home(
      @RequestParam(name = "code", required = false) String code,
      Model model,
      HttpServletResponse response) {

    User user = userService.loadUser(code, response);
    // log.info(user.getName());
    if (user != null) {
      model.addAttribute("user_google", user.getName());
    }

    model.addAttribute("exams", examRepository.findAll());
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    log.info("user: " + auth.getName());
    if (code != null) {
      return "redirect:/home";
    }
    return "home/index";
  }

  @GetMapping("/home/log_in")
  public String login() {
    return "log-in/login";
  }

  @PostMapping("/home/log_in")
  public String log_in(
      @RequestParam String username,
      @RequestParam String password,
      RedirectAttributes redirectAttributes) {

    try {
      authenticationService.authenticate(
          AuthenticationRequest.builder().username(username).password(password).build());
      redirectAttributes.addFlashAttribute("error", "Dang nhap thanh cong");
      return "redirect:/home";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage()); // Trả lỗi về UI
      return "redirect:/home/log_in";
    }
  }
  @GetMapping("/home/must_login")
  public String must_login(){
    return "log-in/must_login";
  }
  @GetMapping("/home/sign_up")
  public String signup() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    log.info("Username: {}", authentication.getName());
    authentication
        .getAuthorities()
        .forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
    return "log-in/sign_up";
  }
}
