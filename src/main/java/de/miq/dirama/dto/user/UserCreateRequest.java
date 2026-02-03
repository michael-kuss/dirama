/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserCreateRequest(
    @Pattern(regexp = "^[a-z0-9_]{1,256}$") String username,
    @NotBlank String password,
    @NotBlank String firstName,
    @NotBlank String lastName,
    String avatarReference) {
  public UserCreateRequest setAvatarReference(String reference) {
    return new UserCreateRequest(
        this.username, this.password, this.firstName, this.lastName, reference);
  }
}
