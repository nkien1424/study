package com.example.study.Service;

import com.example.study.Entity.AppException;
import com.example.study.Entity.ErrorCode;
import com.example.study.Entity.User;
import com.example.study.dto.request.UserRequest;
import com.example.study.dto.response.UserResponse;
import com.example.study.enums.Role;
import com.example.study.repository.UserRepository;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;  // ✅ Tên biến có thể tự chọn
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String client_secret;
    public User createUser(UserRequest userRequest) {
        User user = new User();
        if (userRepository.existsById(userRequest.getName())){
            throw new AppException(ErrorCode.User_Existed);
        }
        PasswordEncoder   passwordEncoder = new BCryptPasswordEncoder(10);
        user.setUsername(userRequest.getUsername());
        user.setName(userRequest.getName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @PostAuthorize("returnObject.username== authentication.name")
    public UserResponse getUser(String username, String password) {

        User user = userRepository.findUserByUsername(username).orElseThrow(() ->  new RuntimeException("User not found"));
         if (passwordEncoder.matches(password, user.getPassword())){
             return UserResponse.builder()
                     .id(user.getId())
                     .username(user.getUsername())
                     .name(user.getName())
                     .password(user.getPassword())
                     .roles(user.getRoles())
                     .build();
         }
            return null;



    }
    public void loadUser(String code){
        if (code != null) {
            try {
                // Bước 1: Đổi code lấy access token
                String accessToken = getAccessTokenFromGoogle(code);

                // Bước 2: Lấy thông tin người dùng
                Map<String, Object> userInfo = getUserInfoFromGoogle(accessToken);

                // Bước 3: Lưu vào database
                String email = (String) userInfo.get("email");
                String name = (String) userInfo.get("name");
                String googleId = (String) userInfo.get("sub"); // ID duy nhất từ Google

                User user = userRepository.findByProviderAndProviderId("google", googleId)
                        .orElseGet(() -> {
                            User newUser = new User();
                            newUser.setEmail(email);
                            newUser.setName(name);
                            newUser.setUsername(name);
                            newUser.setPassword(name);
                            newUser.setProvider("google");
                            newUser.setProviderId(googleId);
                            return userRepository.save(newUser);
                        });


            } catch (Exception e) {
                    log.error("Cannot load user from Google", e);
            }
        }
    }
    private String getAccessTokenFromGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();

        // Tạo request body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId); // Thay bằng client_id của bạn
        params.add("client_secret", client_secret); // Thay bằng client_secret
        params.add("redirect_uri", "http://localhost:8080/home"); // Khớp với URI đã đăng ký
        params.add("grant_type", "authorization_code");

        // Gọi API Google
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token",
                params,
                Map.class
        );

        // Trả về access token
        return (String) response.getBody().get("access_token");
    }
    private Map<String, Object> getUserInfoFromGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        // Tạo header với access token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Gọi API Google
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://openidconnect.googleapis.com/v1/userinfo",
                HttpMethod.GET,
                entity,
                Map.class
        );

        // Trả về thông tin người dùng (email, name, sub, v.v.)
        return response.getBody();
    }
}
