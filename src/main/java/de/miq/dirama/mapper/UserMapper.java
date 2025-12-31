/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.mapper;

import de.miq.dirama.dto.user.UserResponse;
import de.miq.dirama.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserResponse toResponse(UserEntity userEntity);
}
