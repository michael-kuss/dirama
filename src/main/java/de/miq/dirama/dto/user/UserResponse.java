/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.dto.user;

import de.miq.dirama.common.Role;
import java.util.List;

public record UserResponse(
    String id,
    String username,
    String firstName,
    String lastName,
    List<Role> roles,
    Boolean active) {}
