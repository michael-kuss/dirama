/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.dto.user;

public record UserCreateRequest(
    String username, String password, String firstName, String lastName) {}
