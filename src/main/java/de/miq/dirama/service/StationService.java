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
    stationEntity.setActive(true);

    StationEntity savedEntity = stationRepository.save(stationEntity);

    return stationMapper.toResponse(savedEntity);
  }

  public StationResponse get(String stationName) {

    StationEntity stationEntity = getEntity(stationName);

    return stationMapper.toResponse(stationEntity);
  }

  public Page<StationResponse> listAll(Pageable pageable) {

    return stationRepository.findAll(pageable).map(stationMapper::toResponse);
  }

  public StationResponse update(
      String stationName, StationRequest stationRequest, AuthUser authUser) {

    StationEntity stationEntity = getEntity(stationName);
    checkAccessToStation(authUser);

    stationEntity.setName(stationRequest.name());
    // stationEntity.setStationState(StationState.CHANGED);

    StationEntity updatedEntity = stationRepository.save(stationEntity);

    return stationMapper.toResponse(updatedEntity);
  }

  public void delete(String stationName, AuthUser authUser) {

    StationEntity stationEntity = getEntity(stationName);
    checkAccessToStation(authUser);
    stationRepository.deleteById(stationEntity.getName());
  }

  public void toggleStatus(String id) {
    StationEntity entity = getEntity(id);
    entity.setActive(!entity.isActive());
    stationRepository.save(entity);
  }

  private StationEntity getEntity(String stationName) {
    return stationRepository
        .findById(stationName)
        .orElseThrow(() -> new StationNotFoundException(stationName));
  }

  public StationEntity getStationEntityByName(String stationName) {
    return stationRepository
        .findByName(stationName)
        .orElseThrow(() -> new StationNotFoundException(stationName));
  }

  // for this method security responsibilities is scattered between controller and service
  private void checkAccessToStation(AuthUser authUser) {
    if (!authUser.roles().contains(Role.ROLE_ADMIN)) {
      throw new NoAccessException();
    }
  }
}
