/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException() {
    super("Not found");
  }
}
