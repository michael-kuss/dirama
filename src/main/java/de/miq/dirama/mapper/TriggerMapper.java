/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.mapper;

import de.miq.dirama.dto.trigger.TriggerRequest;
import de.miq.dirama.dto.trigger.TriggerResponse;
import de.miq.dirama.entity.TriggerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TriggerMapper {

  @Mapping(target = "station", source = "station.name")
  TriggerResponse toResponse(TriggerEntity triggerEntity);

  TriggerEntity toEntity(TriggerRequest triggerRequest);
}
