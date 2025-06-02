package com.example.study.Controller;

import com.example.study.Entity.User;
import com.example.study.Service.UserService;
import com.example.study.dto.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/createUser")
    public String createUser(Model model) {
        model.addAttribute("user", new UserRequest());
        return "log-in/sign_up";
    }
    @PostMapping("/createUser")
    String createUser(@ModelAttribute UserRequest user, RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }


        return  "redirect:/home/log_in";
    }
}
