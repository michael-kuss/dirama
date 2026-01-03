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
    Boolean active) {}
