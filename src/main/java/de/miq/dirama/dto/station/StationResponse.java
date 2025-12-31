/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.dto.station;

import de.miq.dirama.common.StationState;

public record StationResponse(String id, String name, StationState stationState) {}
