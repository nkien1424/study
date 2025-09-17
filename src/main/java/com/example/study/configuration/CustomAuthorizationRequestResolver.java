package com.example.study.configuration;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
  private final OAuth2AuthorizationRequestResolver defaultResolver;

  public CustomAuthorizationRequestResolver(ClientRegistrationRepository repo) {
    this.defaultResolver =
        new DefaultOAuth2AuthorizationRequestResolver(repo, "/oauth2/authorization");
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
    OAuth2AuthorizationRequest req = defaultResolver.resolve(request);
    if (req != null) {
      req = customizeAuthorizationRequest(req);
    }
    return req;
  }

  @Override
  public OAuth2AuthorizationRequest resolve(
      HttpServletRequest request, String clientRegistrationId) {
    OAuth2AuthorizationRequest req = defaultResolver.resolve(request, clientRegistrationId);
    if (req != null) {
      req = customizeAuthorizationRequest(req);
    }
    return req;
  }

  private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest req) {
    Map<String, Object> additionalParameters = new LinkedHashMap<>(req.getAdditionalParameters());
    additionalParameters.put("prompt", "select_account");

    return OAuth2AuthorizationRequest.from(req).additionalParameters(additionalParameters).build();
  }
}
