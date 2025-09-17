package com.example.study.Controller;

import com.example.study.Entity.User;
import com.example.study.Service.AuthenticationService;
import com.example.study.Service.UserService;
import com.example.study.dto.request.ApiResponse;
import com.example.study.dto.request.AuthenticationRequest;
import com.example.study.dto.request.IntrospectRequest;
import com.example.study.dto.response.AuthenticationResponse;
import com.example.study.dto.response.IntrospectResponse;
import com.example.study.dto.response.UserResponse;
import com.example.study.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.text.ParseException;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
  @Autowired AuthenticationService authenticationService;
  @Autowired UserService userService;
  @Autowired UserRepository userRepository;

  @PostMapping("/getUser")
  ApiResponse<UserResponse> getUser(@RequestBody AuthenticationRequest authenticationRequest)
      throws ParseException {
    UserResponse userResponse =
        userService.getUser(
            authenticationRequest.getUsername(), authenticationRequest.getPassword());
    return ApiResponse.<UserResponse>builder().result(userResponse).build();
  }

  @GetMapping("/")
  public Principal getPrincipal(Principal principal) {
    return principal;
  }

  @PostMapping("/log-in")
  ApiResponse<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request, HttpServletResponse response)
      throws ParseException, JOSEException {

    AuthenticationResponse result = authenticationService.authenticate(request);
    Cookie cookie = new Cookie("jwt", result.getToken());
    cookie.setHttpOnly(false); // không cho JS truy cập
    // cookie.setSecure(true);   // chỉ hoạt động qua HTTPS
    cookie.setPath("/");
    cookie.setMaxAge(33600); // 1 tiếng
    // cookie.setDomain("yourdomain.com"); // nếu cần
    response.addCookie(cookie);
    return ApiResponse.<AuthenticationResponse>builder()
        .result(
            AuthenticationResponse.builder()
                .token(result.getToken())
                .isAuthenticated(result.isAuthenticated())
                .build())
        .build();
  }

  @PostMapping("/introspect")
  ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
      throws ParseException, JOSEException {
    IntrospectResponse result = authenticationService.introspect(request.getToken());
    return ApiResponse.<IntrospectResponse>builder()
        .result(IntrospectResponse.builder().valid(result.isValid()).build())
        .build();
  }

  public String getJwtFromCookies(HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if (cookie.getName().equals("jwt")) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

  @PostMapping("/getCookie")
  public ResponseEntity<?> getUserInfo(HttpServletRequest request)
      throws ParseException, JOSEException {
    String jwt = getJwtFromCookies(request); // đọc từ cookie
    if (jwt != null) {
      String username = authenticationService.getUsernambyCookie(jwt);
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      int id = userRepository.findUserById(Integer.valueOf(auth.getName())).getId();
      log.info(username);
      User user = userRepository.findUserById(Integer.valueOf(auth.getName()));
      return ResponseEntity.ok(Map.of("username", username, "id", id, "role", user.getRoles()));
    }
    return ResponseEntity.status(401).build();
  }
}
