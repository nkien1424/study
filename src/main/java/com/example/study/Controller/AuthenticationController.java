package com.example.study.Controller;

import com.example.study.Service.AuthenticationService;
import com.example.study.Service.UserService;
import com.example.study.dto.request.ApiResponse;
import com.example.study.dto.request.AuthenticationRequest;
import com.example.study.dto.request.IntrospectRequest;
import com.example.study.dto.response.AuthenticationResponse;
import com.example.study.dto.response.IntrospectResponse;
import com.example.study.dto.response.UserResponse;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserService userService;
    @PostMapping("/getUser")
    ApiResponse<UserResponse> getUser(@RequestBody AuthenticationRequest authenticationRequest) throws ParseException {
        UserResponse userResponse = userService.getUser(authenticationRequest.getUsername(),authenticationRequest.getPassword());
        return ApiResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }
    @RequestMapping("/")
    public Principal getPrincipal(Principal principal) {
        return principal;
    }
    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        AuthenticationResponse result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(AuthenticationResponse.builder()
                        .token(result.getToken())
                        .isAuthenticated(result.isAuthenticated())
                        .build())
                .build();
    }
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspect(request.getToken());
        return ApiResponse.<IntrospectResponse>builder()
                .result(IntrospectResponse.builder()
                        .valid(result.isValid())

                        .build())
                .build();
    }
}
