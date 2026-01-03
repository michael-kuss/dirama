/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.security.apikey;

import de.miq.dirama.common.AuthConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthenticationConverter implements AuthenticationConverter {

  @Override
  public Authentication convert(HttpServletRequest request) {

    String apiKey = request.getHeader(AuthConstants.API_KEY_AUTHORIZATION_HEADER);

    if (apiKey == null || apiKey.isEmpty()) {
      return null;
    }

    if (apiKey.length() > 1024) {
      throw new BadCredentialsException("Invalid apiKey");
    }

    return ApiKeyAuthentication.unauthenticated(apiKey);
  }
}
