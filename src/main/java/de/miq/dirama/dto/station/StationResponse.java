/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.station;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.miq.dirama.common.StationState;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StationResponse(String id, String name, StationState stationState) {}
