/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.station;

import jakarta.validation.constraints.Pattern;

public record StationRequest(
    @Pattern(regexp = "^[a-z0-9_]{1,256}$") String name, String avatarReference, Boolean active) {
  public StationRequest newWithAvatarReference(String reference) {
    return new StationRequest(this.name, reference, this.active);
  }
}
