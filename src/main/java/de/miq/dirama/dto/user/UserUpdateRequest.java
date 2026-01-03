/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserUpdateRequest(String username, String firstName, String lastName) {}
