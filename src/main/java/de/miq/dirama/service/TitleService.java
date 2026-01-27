/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.service;

import de.miq.dirama.dto.title.TitleRequest;
import de.miq.dirama.dto.title.TitleResponse;
import de.miq.dirama.entity.StationEntity;
import de.miq.dirama.entity.TitleEntity;
import de.miq.dirama.mapper.TitleMapper;
import de.miq.dirama.repository.TitleRepository;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TitleService {
  private final TitleRepository titleRepository;
  private final TitleMapper titleMapper;
  private final StationService stationService;
  private final TriggerService triggerService;

  public TitleService(
      TitleRepository titleRepository,
      TitleMapper titleMapper,
      StationService stationService,
      TriggerService triggerService) {
    this.titleRepository = titleRepository;
    this.titleMapper = titleMapper;
    this.stationService = stationService;
    this.triggerService = triggerService;
  }

  public TitleResponse create(
      String station, TitleRequest titleRequest, boolean ignoreNow, boolean trigger) {
    ZonedDateTime now = ZonedDateTime.now();

    if (!ignoreNow) {
      ZonedDateTime before = now.minusMinutes(10);
      ZonedDateTime after = now.plusMinutes(10);

      if (!(titleRequest.titleDate().isAfter(after) && titleRequest.titleDate().isBefore(before))) {
        throw new IllegalStateException("Date not now");
      }
    }

    StationEntity stationEntity = stationService.getStationEntityByName(station);

    TitleEntity titleEntity = titleMapper.toEntity(titleRequest);
    titleEntity.setStation(stationEntity);

    TitleEntity result = titleRepository.save(titleEntity);

    log.info("Added {}", titleRequest);

    if (trigger) {
      triggerService.executeTriggers(stationEntity);
    }

    return titleMapper.toResponse(result);
  }

  public Page<TitleResponse> listAll(Pageable pageable) {
    return titleRepository.findAll(pageable).map(titleMapper::toResponse);
  }
}
