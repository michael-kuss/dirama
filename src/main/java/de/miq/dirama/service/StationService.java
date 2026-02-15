/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.service;

import de.miq.dirama.common.Role;
import de.miq.dirama.dto.station.StationRequest;
import de.miq.dirama.dto.station.StationResponse;
import de.miq.dirama.entity.StationEntity;
import de.miq.dirama.exception.ExistingException;
import de.miq.dirama.exception.NoAccessException;
import de.miq.dirama.exception.StationNotFoundException;
import de.miq.dirama.mapper.StationMapper;
import de.miq.dirama.repository.StationRepository;
import de.miq.dirama.security.user.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StationService {

  private final StationRepository stationRepository;

  private final StationMapper stationMapper;

  public StationService(StationRepository stationRepository, StationMapper stationMapper) {
    this.stationRepository = stationRepository;
    this.stationMapper = stationMapper;
  }

  public StationResponse create(StationRequest stationRequest) {
    stationRepository
        .findById(stationRequest.name())
        .ifPresent(
            s -> {
              throw new ExistingException();
            });

    StationEntity stationEntity = new StationEntity();
    stationEntity.setName(stationRequest.name());
    stationEntity.setAvatarReference(stationRequest.avatarReference());
    stationEntity.setActive(true);

    StationEntity savedEntity = stationRepository.save(stationEntity);

    return stationMapper.toResponse(savedEntity);
  }

  public StationResponse get(String id) {

    StationEntity stationEntity = getEntity(id);

    return stationMapper.toResponse(stationEntity);
  }

  public Page<StationResponse> listAll(Pageable pageable) {

    return stationRepository.findAll(pageable).map(stationMapper::toResponse);
  }

  public StationResponse update(String id, StationRequest stationRequest, AuthUser authUser) {
    checkAdminAccess(authUser);

    StationEntity stationEntity = getEntity(id);

    if (!stationEntity.getName().equals(stationRequest.name())) {
      stationRepository
          .findByName(stationRequest.name())
          .ifPresent(
              u -> {
                throw new ExistingException();
              });
    }
    stationMapper.update(stationEntity, stationRequest);

    StationEntity updatedEntity = stationRepository.save(stationEntity);

    return stationMapper.toResponse(updatedEntity);
  }

  public void delete(String id, AuthUser authUser) {
    checkAdminAccess(authUser);

    StationEntity stationEntity = getEntity(id);
    stationRepository.deleteById(stationEntity.getId());
  }

  public void toggleStatus(String id, AuthUser authUser) {
    checkAdminAccess(authUser);

    StationEntity entity = getEntity(id);
    entity.setActive(!entity.isActive());
    stationRepository.save(entity);
  }

  private StationEntity getEntity(String id) {
    return stationRepository.findById(id).orElseThrow(() -> new StationNotFoundException(id));
  }

  public StationEntity getStationEntityByName(String stationName) {
    return stationRepository
        .findByName(stationName)
        .orElseThrow(() -> new StationNotFoundException(stationName));
  }

  // for this method security responsibilities is scattered between controller and service
  private void checkAdminAccess(AuthUser authUser) {
    if (!authUser.roles().contains(Role.ROLE_ADMIN)) {
      throw new NoAccessException();
    }
  }
}
