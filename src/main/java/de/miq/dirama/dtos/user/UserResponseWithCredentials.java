/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.dtos.user;

public record UserResponseWithCredentials(UserResponse userResponse, String passwordHash) {}
