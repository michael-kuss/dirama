/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.user;

public record UserResponseWithCredentials(UserResponse userResponse, String passwordHash) {}
