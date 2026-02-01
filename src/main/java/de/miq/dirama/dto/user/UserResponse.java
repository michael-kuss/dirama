/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.miq.dirama.common.Role;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
    String id,
    String username,
    String firstName,
    String lastName,
    List<Role> roles,
    Boolean active) {
  public String abbreviation() {
    return firstLetter(firstName, username, 1) + firstLetter(lastName, username, 2);
  }

  private String firstLetter(String either, String or, int count) {
    if (count < 1) {
      return "X";
    }

    if (either != null && either.length() >= count) {
      return either.substring(0, 1).toUpperCase();
    } else if (or != null && or.length() >= count) {
      return or.substring(count - 1, count).toUpperCase();
    }
    return "X";
  }
}
