/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.user;

public record UserPasswordUpdateRequest(String oldPassword, String newPassword) {}
