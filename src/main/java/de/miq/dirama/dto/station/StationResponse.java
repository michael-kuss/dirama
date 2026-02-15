/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.station;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StationResponse(String id, String name, String avatarReference, boolean active) {}
