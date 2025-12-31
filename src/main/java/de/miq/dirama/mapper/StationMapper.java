/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.mapper;

import de.miq.dirama.dto.station.StationResponse;
import de.miq.dirama.entity.StationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StationMapper {

  StationResponse toResponse(StationEntity stationEntity);
}
