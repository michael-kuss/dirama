/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserUpdateRequest(
    @Pattern(regexp = "^[a-z0-9_]{1,256}$") String username,
    @NotBlank String firstName,
    @NotBlank String lastName,
    String avatarReference) {
  public UserUpdateRequest newWithAvatarReference(String reference) {
    return new UserUpdateRequest(this.username, this.firstName, this.lastName, reference);
  }
}
