/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.service;

import de.miq.dirama.common.Role;
import de.miq.dirama.dto.user.*;
import de.miq.dirama.entity.UserEntity;
import de.miq.dirama.exception.ExistingException;
import de.miq.dirama.exception.NoAccessException;
import de.miq.dirama.exception.NotFoundException;
import de.miq.dirama.mapper.UserMapper;
import de.miq.dirama.repository.UserRepository;
import de.miq.dirama.security.exceptions.ApplicationAuthenticationException;
import de.miq.dirama.security.user.AuthUser;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

  private final String defaultAdminUsername;

  private final String defaultAdminPassword;

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  private final PasswordEncoder passwordEncoder;

  public UserService(
      @Value("${admin.default.username}") String defaultAdminUsername,
      @Value("${admin.default.password}") String defaultAdminPassword,
      UserRepository userRepository,
      UserMapper userMapper,
      PasswordEncoder passwordEncoder) {
    this.defaultAdminUsername = defaultAdminUsername;
    this.defaultAdminPassword = defaultAdminPassword;
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
  }

  public UserResponse create(UserCreateRequest userCreateRequest) {
    userRepository
        .findByUsername(userCreateRequest.username())
        .ifPresent(
            u -> {
              throw new ExistingException();
            });

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(userCreateRequest.username());
    userEntity.setPasswordHash(passwordEncoder.encode(userCreateRequest.password()));
    userEntity.setFirstName(userCreateRequest.firstName());
    userEntity.setLastName(userCreateRequest.lastName());
    userEntity.setRoles(Set.of(Role.ROLE_USER));
    if (userCreateRequest.avatarReference() != null) {
      userEntity.setAvatarReference(userCreateRequest.avatarReference());
    }
    userEntity.setActive(true);

    UserEntity savedEntity = userRepository.save(userEntity);

    return userMapper.toResponse(savedEntity);
  }

  public UserResponse syncUser(UserSyncRequest userSyncRequest) {

    UserEntity userEntity =
        userRepository
            .findById(userSyncRequest.userId())
            .orElseGet(
                () -> {
                  UserEntity newUserEntity = new UserEntity();
                  newUserEntity.setId(userSyncRequest.userId());
                  newUserEntity.setUsername(userSyncRequest.username());
                  newUserEntity.setFirstName(userSyncRequest.firstName());
                  newUserEntity.setLastName(userSyncRequest.lastName());
                  newUserEntity.setRoles(Set.of(Role.ROLE_USER));
                  newUserEntity.setActive(true);
                  return userRepository.save(newUserEntity);
                });

    return userMapper.toResponse(userEntity);
  }

  public void changeUserPassword(
      UserPasswordUpdateRequest passwordUpdateRequest, AuthUser authUser) {

    if (!authUser.isInternalUser()) {
      throw new ApplicationAuthenticationException(
          "Password can be updated only for internal users");
    }

    UserEntity userEntity = getEntity(authUser.userId());

    if (!passwordEncoder.matches(
        passwordUpdateRequest.oldPassword(), userEntity.getPasswordHash())) {
      throw new ApplicationAuthenticationException("Old password is incorrect");
    }

    userEntity.setPasswordHash(passwordEncoder.encode(passwordUpdateRequest.newPassword()));

    userRepository.save(userEntity);
  }

  public UserResponseWithCredentials getUserCredentialsByUsername(String username) {
    UserEntity userEntity =
        userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    if (!userEntity.isActive()) {
      throw new NotFoundException();
    }
    return new UserResponseWithCredentials(
        userMapper.toResponse(userEntity), userEntity.getPasswordHash());
  }

  public UserResponse updateUser(String userId, UserUpdateRequest userUpdateRequest) {

    UserEntity userEntity = getEntity(userId);

    userEntity.setUsername(userUpdateRequest.username());
    userEntity.setFirstName(userUpdateRequest.firstName());
    userEntity.setLastName(userUpdateRequest.lastName());

    UserEntity updatedEntity = userRepository.save(userEntity);

    return userMapper.toResponse(updatedEntity);
  }

  public UserResponse getUserById(String userId) {

    UserEntity userEntity = getEntity(userId);
    return userMapper.toResponse(userEntity);
  }

  public Page<UserResponse> listAll(Pageable pageable) {

    return userRepository.findAll(pageable).map(userMapper::toResponse);
  }

  public UserResponse activateUser(String userId) {

    UserEntity userEntity = getEntity(userId);
    userEntity.setActive(true);

    UserEntity updatedEntity = userRepository.save(userEntity);
    return userMapper.toResponse(updatedEntity);
  }

  public UserResponse deactivateUser(String userId) {

    UserEntity userEntity = getEntity(userId);
    userEntity.setActive(false);

    UserEntity updatedEntity = userRepository.save(userEntity);
    return userMapper.toResponse(updatedEntity);
  }

  public UserResponse promoteUserToAdmin(String userId) {

    UserEntity userEntity = getEntity(userId);
    userEntity.getRoles().add(Role.ROLE_ADMIN);

    UserEntity updatedEntity = userRepository.save(userEntity);
    return userMapper.toResponse(updatedEntity);
  }

  public void createDefaultAdminIfNotExist() {

    boolean anyAdminExist = userRepository.isAnyAdminExist();

    if (anyAdminExist) {
      log.info("Admin already exist. Skipping creation of default admin user");
      return;
    }

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(defaultAdminUsername);
    userEntity.setPasswordHash(passwordEncoder.encode(defaultAdminPassword));
    userEntity.setFirstName("Admin");
    userEntity.setLastName("Admin");
    userEntity.setRoles(Set.of(Role.ROLE_ADMIN));
    userEntity.setActive(true);

    UserEntity savedUser = userRepository.save(userEntity);

    log.info("Default admin user was created with id={}", savedUser.getId());
  }

  private UserEntity getEntity(String userId) {

    return userRepository.findById(userId).orElseThrow(NotFoundException::new);
  }

  public void toggleStatus(String username) {
    UserEntity entity = getEntity(username);
    entity.setActive(!entity.isActive());
    userRepository.save(entity);
  }

  public void delete(String id, AuthUser authUser) {
    checkAccessToStation(authUser);
    userRepository.deleteById(id);
  }

  // for this method security responsibilities is scattered between controller and service
  private void checkAccessToStation(AuthUser authUser) {
    if (!authUser.roles().contains(Role.ROLE_ADMIN)) {
      throw new NoAccessException();
    }
  }
}
