/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class ApplicationAuthenticationException extends AuthenticationException {

  public ApplicationAuthenticationException(String msg) {
    super(msg);
  }
}
