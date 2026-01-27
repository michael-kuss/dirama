/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.exception;

public class StationNotFoundException extends RuntimeException {

  public StationNotFoundException(String station) {
    super(station + " not found");
  }
}
