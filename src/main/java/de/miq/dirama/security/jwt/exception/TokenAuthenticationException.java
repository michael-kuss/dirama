/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenAuthenticationException extends AuthenticationException {
  public TokenAuthenticationException(String message) {
    super(message);
  }
}
