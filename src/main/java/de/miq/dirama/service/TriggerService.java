/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.service;

import de.miq.dirama.dto.trigger.TriggerRequest;
import de.miq.dirama.dto.trigger.TriggerResponse;
import de.miq.dirama.entity.TriggerEntity;
import de.miq.dirama.mapper.TriggerMapper;
import de.miq.dirama.repository.TriggerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TriggerService {
  private TriggerRepository triggerRepository;
  private StationService stationService;
  private TriggerMapper triggerMapper;

  public TriggerService(
      TriggerRepository triggerRepository,
      StationService stationService,
      TriggerMapper triggerMapper) {
    this.triggerRepository = triggerRepository;
    this.stationService = stationService;
    this.triggerMapper = triggerMapper;
  }

  public TriggerResponse create(String station, TriggerRequest request) {
    TriggerEntity entity = new TriggerEntity();

    entity.setTrigger(request.trigger());
    entity.setStation(stationService.getStationEntityByName(station));
    entity.setProperties(request.properties());
    entity.setActive(request.active());

    TriggerEntity result = triggerRepository.save(entity);
    return triggerMapper.toResponse(result);
  }

  public Page<TriggerResponse> listAll(Pageable pageable) {
    return triggerRepository.findAll(pageable).map(triggerMapper::toResponse);
  }

  public Page<TriggerResponse> listAllByStation(String station, Pageable pageable) {
    return triggerRepository
        .findAllByStation(stationService.getStationEntityByName(station), pageable)
        .map(triggerMapper::toResponse);
  }
}
