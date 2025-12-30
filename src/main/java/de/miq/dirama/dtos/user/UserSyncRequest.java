/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.dtos.user;

public record UserSyncRequest(String userId, String username, String firstName, String lastName) {}
