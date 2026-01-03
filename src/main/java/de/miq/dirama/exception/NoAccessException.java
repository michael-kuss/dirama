/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.exception;

import org.springframework.security.access.AccessDeniedException;

public class NoAccessException extends AccessDeniedException {

  public NoAccessException() {
    super("No access");
  }
}
