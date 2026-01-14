/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.mapper;

import de.miq.dirama.dto.trigger.TriggerResponse;
import de.miq.dirama.entity.TriggerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TriggerMapper {

  @Mapping(target = "station", source = "station.name")
  TriggerResponse toResponse(TriggerEntity triggerEntity);

  // TitleEntity toEntity(TitleRequest titleRequest);
}
