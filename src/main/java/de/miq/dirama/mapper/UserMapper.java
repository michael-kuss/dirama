/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.mapper;

import de.miq.dirama.dto.user.UserResponse;
import de.miq.dirama.dto.user.UserUpdateRequest;
import de.miq.dirama.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  UserResponse toResponse(UserEntity userEntity);

  void update(@MappingTarget UserEntity userEntity, UserUpdateRequest userUpdateRequest);
}
