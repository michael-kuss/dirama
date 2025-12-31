/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.mapper;

import de.miq.dirama.dto.title.TitleResponse;
import de.miq.dirama.entity.TitleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TitleMapper {

  @Mapping(target = "station", source = "station.name")
  TitleResponse toResponse(TitleEntity titleEntity);

  // TitleEntity toEntity(TitleRequest titleRequest);
}
