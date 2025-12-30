/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.mappers;

import de.miq.dirama.dtos.item.ItemResponse;
import de.miq.dirama.enitities.ItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

  ItemResponse toResponse(ItemEntity itemEntity);
}
