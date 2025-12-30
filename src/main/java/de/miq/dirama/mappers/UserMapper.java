/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.mappers;

import de.miq.dirama.dtos.user.UserResponse;
import de.miq.dirama.enitities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserResponse toResponse(UserEntity userEntity);
}
