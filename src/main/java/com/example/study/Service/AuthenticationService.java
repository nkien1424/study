package com.example.study.Service;

import com.example.study.Entity.AppException;
import com.example.study.Entity.ErrorCode;
import com.example.study.Entity.User;
import com.example.study.dto.request.ApiResponse;
import com.example.study.dto.request.AuthenticationRequest;
import com.example.study.dto.response.AuthenticationResponse;
import com.example.study.dto.response.IntrospectResponse;
import com.example.study.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.StringJoiner;

@Service
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    UserRepository userRepository;

    @Value("${jwt.signerKey}")
    private  String Signer_Key ;
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        var user = userRepository.findUserByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.User_Not_Existed));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(),user.getPassword());
        if(!authenticated) {
            throw new AppException(ErrorCode.UnAuthenticated);
        }
        return AuthenticationResponse.builder()
                .token(generateToken(user))
                .isAuthenticated(authenticated)
                .build();
    }
    public String generateToken(User user) {
        JWSHeader jwsHeader=new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet=new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("study.com")
                .expirationTime(new java.util.Date(System.currentTimeMillis()+1000*60*60*24))
                .claim("roles",user.getRoles())
                .build();
        Payload payload=new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);
        try {
            jwsObject.sign(new MACSigner(Signer_Key.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }

    }
    public IntrospectResponse introspect(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(Signer_Key.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean valid = signedJWT.verify(verifier);
        return IntrospectResponse.builder()
                .valid(valid&&expirationTime.after(new Date()))
                .build();
    }
    private String generateScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }
}
