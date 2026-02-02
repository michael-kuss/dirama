/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.station;

import jakarta.validation.constraints.Pattern;

public record StationRequest(@Pattern(regexp = "^[a-z0-9_]{1,256}$") String name, Boolean active) {}
