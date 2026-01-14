/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.service;

import de.miq.dirama.common.Role;
import de.miq.dirama.common.StationState;
import de.miq.dirama.dto.station.StationRequest;
import de.miq.dirama.dto.station.StationResponse;
import de.miq.dirama.entity.StationEntity;
import de.miq.dirama.exception.NoAccessException;
import de.miq.dirama.exception.NotFoundException;
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

  public StationResponse createStation(StationRequest stationRequest) {

    StationEntity stationEntity = new StationEntity();
    stationEntity.setName(stationRequest.name());
    stationEntity.setStationState(StationState.ONLINE);

    StationEntity savedEntity = stationRepository.save(stationEntity);

    return stationMapper.toResponse(savedEntity);
  }

  public StationResponse getStation(String stationName) {

    StationEntity stationEntity = getStationEntity(stationName);

    return stationMapper.toResponse(stationEntity);
  }

  public Page<StationResponse> listAllStations(Pageable pageable) {

    return stationRepository.findAll(pageable).map(stationMapper::toResponse);
  }

  public StationResponse updateStation(
      String stationName, StationRequest stationRequest, AuthUser authUser) {

    StationEntity stationEntity = getStationEntity(stationName);
    checkAccessToStation(authUser);

    stationEntity.setName(stationRequest.name());
    // stationEntity.setStationState(StationState.CHANGED);

    StationEntity updatedEntity = stationRepository.save(stationEntity);

    return stationMapper.toResponse(updatedEntity);
  }

  public void deleteStation(String stationName, AuthUser authUser) {

    StationEntity stationEntity = getStationEntity(stationName);
    checkAccessToStation(authUser);
    stationRepository.deleteById(stationEntity.getName());
  }

  private StationResponse setStationState(String stationName, StationState state) {
    StationEntity stationEntity = getStationEntity(stationName);
    stationEntity.setStationState(state);

    StationEntity updatedEntity = stationRepository.save(stationEntity);

    return stationMapper.toResponse(updatedEntity);
  }

  private StationEntity getStationEntity(String stationName) {
    return stationRepository.findById(stationName).orElseThrow(NotFoundException::new);
  }

  public StationEntity getStationEntityByName(String stationName) {
    return stationRepository.findByName(stationName).orElseThrow(NotFoundException::new);
  }

  // for this method security responsibilities is scattered between controller and service
  private void checkAccessToStation(AuthUser authUser) {
    if (!authUser.roles().contains(Role.ROLE_ADMIN)) {
      throw new NoAccessException();
    }
  }
}
