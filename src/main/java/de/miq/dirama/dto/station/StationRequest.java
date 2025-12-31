/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.dto.station;

import de.miq.dirama.common.StationState;
import jakarta.validation.constraints.NotNull;

public record StationRequest(@NotNull String name, StationState stationState) {}
