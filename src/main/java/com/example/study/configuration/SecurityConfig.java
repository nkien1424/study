package com.example.study.configuration;

import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Value("${jwt.signerKey}")
  private String signerKey;

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      InMemoryClientRegistrationRepository clientRegistrationRepository,
      JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter)
      throws Exception {

    http.authorizeHttpRequests(
        auth ->
            auth.requestMatchers(new AntPathRequestMatcher("/home"))
                .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/blog"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/blog/**"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/vocabulary/**"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/vocabulary/"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/home/**"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/exam/**"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/exam"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/admin/**"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/admin"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/user/**"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/home/log_in"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/auth/**"))
                .permitAll() // Dùng AntPathRequestMatcher thay vì mvcMatcherBuilder
                .requestMatchers(
                    new AntPathRequestMatcher("/css/**"),
                    new AntPathRequestMatcher("/js/**"),
                    new AntPathRequestMatcher("/images/**"),
                    new AntPathRequestMatcher("/sound/**"),
                    new AntPathRequestMatcher("/webjars/**"))
                .permitAll()
                .anyRequest()
                .authenticated());
    http.oauth2ResourceServer(
        oauth2 ->
            oauth2.jwt(
                jwtConfigurer ->
                    jwtConfigurer
                        .decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())));
    http.oauth2Login(
        oauth2 ->
            oauth2.authorizationEndpoint(
                authorization ->
                    authorization
                        .baseUri("/oauth2/authorization")
                        .authorizationRequestResolver(
                            new CustomAuthorizationRequestResolver(clientRegistrationRepository))));
    http.exceptionHandling(
        ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
    http.addFilterBefore(
        jwtCookieAuthenticationFilter, SecurityContextHolderAwareRequestFilter.class);
    http.csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }

  private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(
        jwt -> {
          List<String> roles = jwt.getClaim("roles");
          return roles.stream()
              .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
              .collect(Collectors.toList());
        });
    return converter;
  }

  @Bean
  JwtDecoder jwtDecoder() {
    SecretKeySpec secretKey = new SecretKeySpec(signerKey.getBytes(), "HS512");
    NimbusJwtDecoder nimbusJwtDecoder =
        NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
    return nimbusJwtDecoder;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }
}
