/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.service;

import de.miq.dirama.dto.trigger.TriggerRequest;
import de.miq.dirama.dto.trigger.TriggerResponse;
import de.miq.dirama.entity.StationEntity;
import de.miq.dirama.entity.TriggerEntity;
import de.miq.dirama.exception.NotFoundException;
import de.miq.dirama.mapper.TriggerMapper;
import de.miq.dirama.repository.TitleRepository;
import de.miq.dirama.repository.TriggerRepository;
import de.miq.dirama.service.trigger.LogTrigger;
import de.miq.dirama.service.trigger.RenderHtmlPlaylistTrigger;
import de.miq.dirama.service.trigger.SendFileToFtpTrigger;
import de.miq.dirama.service.trigger.TriggerInterface;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TriggerService {
  private final TitleRepository titleRepository;
  private final TriggerRepository triggerRepository;
  private final StationService stationService;
  private final TriggerMapper triggerMapper;

  public TriggerService(
      TriggerRepository triggerRepository,
      StationService stationService,
      TriggerMapper triggerMapper,
      TitleRepository titleRepository) {
    this.triggerRepository = triggerRepository;
    this.stationService = stationService;
    this.triggerMapper = triggerMapper;
    this.titleRepository = titleRepository;
  }

  public TriggerResponse create(String station, TriggerRequest request) {

    TriggerEntity entity = triggerMapper.toEntity(request); // new TriggerEntity();

    // entity.setTrigger(request.trigger());
    entity.setStation(stationService.getStationEntityByName(station));
    // entity.setProperties(request.properties());
    // entity.setActive(request.active());

    build(entity);

    log.info("Create trigger entity: {}", entity);
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

  public void toggleStatus(String id) {
    TriggerEntity entity = getEntity(id);
    entity.setActive(!entity.isActive());
    triggerRepository.save(entity);
  }

  public TriggerResponse update(String id, TriggerRequest triggerRequest) {
    TriggerEntity triggerEntity = getEntity(id);

    TriggerEntity updatedEntity = triggerRepository.save(triggerEntity);

    return triggerMapper.toResponse(updatedEntity);
  }

  public void executeTriggers(StationEntity station) {
    PageRequest pageRequest = PageRequest.of(0, 10);

    Page<TriggerEntity> page;

    do {
      page = triggerRepository.findAllByStation(station, pageRequest);

      page.getContent().forEach(t -> build(t).ifPresent(i -> i.execute(station)));

      pageRequest = pageRequest.next();
    } while (page.hasNext());
  }

  public Optional<TriggerInterface> build(@NotNull TriggerEntity triggerEntity) {

    return switch (triggerEntity.getTrigger()) {
      case RENDER_HTML_PLAYLIST -> new RenderHtmlPlaylistTrigger(titleRepository).to(triggerEntity);
      case SEND_FILE_TO_FTP -> new SendFileToFtpTrigger(titleRepository).to(triggerEntity);
      case LOG -> new LogTrigger(titleRepository).to(triggerEntity);
    };
  }

  private TriggerEntity getEntity(String id) {
    return triggerRepository.findById(id).orElseThrow(NotFoundException::new);
  }

  public void delete(String id) {
    triggerRepository.deleteById(id);
  }
}
