/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.mapper;

import de.miq.dirama.dto.title.TitleRequest;
import de.miq.dirama.dto.title.TitleResponse;
import de.miq.dirama.entity.TitleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TitleMapper {

  @Mapping(target = "station", source = "station.name")
  TitleResponse toResponse(TitleEntity titleEntity);

  TitleEntity toEntity(TitleRequest titleRequest);
}
