/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.security.basic;

import de.miq.dirama.common.AuthConstants;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthenticationConverter implements AuthenticationConverter {

  @Override
  public Authentication convert(HttpServletRequest request) {

    String authenticationHeader = request.getHeader(AuthConstants.JWT_AUTHORIZATION_HEADER);

    if (authenticationHeader == null || authenticationHeader.isEmpty()) {
      return null;
    }

    if (!authenticationHeader.startsWith("Basic")) {
      return null;
    }

    String basicToken = stripBearerPrefix(authenticationHeader);
    String[] loginTokens = basicToken.split(":");

    return UsernamePasswordAuthenticationToken.unauthenticated(loginTokens[0], loginTokens[1]);
  }

  String stripBearerPrefix(String token) {
    return new String(Base64.getDecoder().decode(token.substring(6)));
  }
}
