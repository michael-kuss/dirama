/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.mapper;

import de.miq.dirama.dto.station.StationRequest;
import de.miq.dirama.dto.station.StationResponse;
import de.miq.dirama.entity.StationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StationMapper {

  StationResponse toResponse(StationEntity stationEntity);

  void update(@MappingTarget StationEntity stationEntity, StationRequest stationRequest);
}
