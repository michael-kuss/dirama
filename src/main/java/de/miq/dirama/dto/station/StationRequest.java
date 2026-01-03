/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.station;

import de.miq.dirama.common.StationState;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StationRequest(
    @NotNull @Size(min = 1, max = 512) String name, StationState stationState) {}
