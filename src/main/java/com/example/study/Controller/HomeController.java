package com.example.study.Controller;

import com.example.study.Entity.User;
import com.example.study.Service.AuthenticationService;
import com.example.study.Service.UserService;
import com.example.study.dto.request.AuthenticationRequest;
import com.example.study.repository.ExamRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.http.HttpHeaders;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    ExamRepository examRepository;


    @GetMapping("")
    public String home(@RequestParam(name = "code", required = false) String code, Model model, HttpServletResponse response) {

        User user = userService.loadUser(code,response);
        //log.info(user.getName());
        if(user != null) {
            model.addAttribute("user_google", user.getName());
        }

        model.addAttribute("exams", examRepository.findAll());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("user: " + auth.getName());
        if(code!= null) {
            return  "redirect:/home";
        }
        return "home/index";
    }



    @GetMapping("/log_in")
    public String login() {
        return "log-in/login";
    }
    @PostMapping("/log_in")
    public String log_in(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {

        try {
            authenticationService.authenticate(AuthenticationRequest.builder()
                            .username(username)
                            .password(password)
                    .build());
            redirectAttributes.addFlashAttribute("error", "Dang nhap thanh cong");
            return "redirect:/home";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage()); // Trả lỗi về UI
            return "redirect:/home/log_in";
        }

    }

    @GetMapping("/sign_up")
    public String signup() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return "log-in/sign_up";
    }

}
