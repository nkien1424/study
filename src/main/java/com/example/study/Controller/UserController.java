package com.example.study.Controller;

import com.example.study.Entity.ChangePasswordRequest;
import com.example.study.Entity.User;
import com.example.study.Service.UserService;
import com.example.study.dto.request.UserRequest;
import com.example.study.repository.UserRepository;
import com.example.study.repository.User_ExamRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

  @Autowired private UserService userService;
  @Autowired private User_ExamRepository examRepository;
  @Autowired private UserRepository UserRepository;
  @Autowired private UserRepository userRepository;
  @Autowired PasswordEncoder passwordEncoder;

  @GetMapping("/profile")
  public String profile(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("user", auth.getName());
    User user = userRepository.findUserById(Integer.valueOf(auth.getName()));
    log.info(user.getUsername());

    model.addAttribute("user", user);
    model.addAttribute("exams", examRepository.findAllByUserId(Integer.valueOf(auth.getName())));
    return "user/user_profile";
  }

  @GetMapping("/edit_information")
  public String editInformation(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("user", auth.getName());
    User user = userRepository.findUserById(Integer.valueOf(auth.getName()));
    log.info(user.getUsername());
    model.addAttribute("user", user);
    model.addAttribute("exams", examRepository.findAllByUserId(Integer.valueOf(auth.getName())));
    return "user/edit_information";
  }

  //    @PostMapping("update_profile")
  //    public String updateProfile(@ModelAttribute("user") User user, RedirectAttributes
  // redirectAttributes) {
  //
  //    }
  @GetMapping("/createUser")
  public String createUser(Model model) {
    model.addAttribute("user", new UserRequest());
    return "log-in/sign_up";
  }

  @PostMapping("/createUser")
  @ResponseBody
  public ResponseEntity<?> createUser(@RequestBody UserRequest user) {
    try {
      userService.createUser(user);
      return ResponseEntity.ok(Map.of(
              "code", 0,
              "message", "Đăng ký thành công!"
      ));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of(
              "code", 1,
              "message", e.getMessage()
      ));
    }
  }


  @PostMapping("/change-password")
  ResponseEntity<?> changePassword(
      @RequestBody ChangePasswordRequest request,
      RedirectAttributes redirectAttributes,
      Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = userRepository.findUserById(Integer.valueOf(auth.getName()));

    if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {

      user.setPassword(passwordEncoder.encode(request.getNewPassword()));
      userRepository.save(user);
      redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Mật khẩu hiện tại không đúng"));
    }
    return ResponseEntity.ok(Map.of("message", "Đổi mật khẩu thành công"));
  }

  @PostMapping("/change-name")
  public ResponseEntity<?> changeName(@RequestBody Map<String, String> body) {
    String newName = body.get("newName");
    log.info("Ten moi : {}", newName);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    int userId = Integer.parseInt(auth.getName());
    User user = userRepository.findUserById(userId);

    user.setName(newName);
    userRepository.save(user);

    return ResponseEntity.ok().body(Map.of("message", "Đổi tên thành công"));
  }

  @PostMapping("/logout")
  public String logout(HttpServletResponse response) {
    Cookie cookie = new Cookie("jwt", null);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(0); // Xóa cookie ngay lập tức
    response.addCookie(cookie);

    return "home/index";
  }
}
