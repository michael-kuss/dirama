/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.exception;

import java.util.Arrays;

public class TriggerKeyMissingException extends RuntimeException {
  public TriggerKeyMissingException(String[] keys) {
    super("Trigger keys missing : " + Arrays.toString(keys));
  }
}
