/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.mapper;

import de.miq.dirama.dto.user.UserResponse;
import de.miq.dirama.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  UserResponse toResponse(UserEntity userEntity);
}
