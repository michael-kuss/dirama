/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.service;

import de.miq.dirama.dto.title.TitleRequest;
import de.miq.dirama.dto.title.TitleResponse;
import de.miq.dirama.entity.TitleEntity;
import de.miq.dirama.mapper.TitleMapper;
import de.miq.dirama.repository.TitleRepository;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TitleService {
  private final TitleRepository titleRepository;
  private final TitleMapper titleMapper;
  private final StationService stationService;

  public TitleService(
      TitleRepository titleRepository, TitleMapper titleMapper, StationService stationService) {
    this.titleRepository = titleRepository;
    this.titleMapper = titleMapper;
    this.stationService = stationService;
  }

  public TitleResponse createTitle(String station, TitleRequest titleRequest) {
    TitleEntity titleEntity = new TitleEntity();
    titleEntity.setStation(stationService.getStationEntityByName(station));
    titleEntity.setArtist(titleRequest.artist());
    titleEntity.setTitle(titleRequest.title());
    titleEntity.setDabImage(titleRequest.dabImage());
    titleEntity.setWebImage(titleRequest.webImage());
    titleEntity.setTitleDate(ZonedDateTime.now());
    titleEntity.setAdditional1(titleRequest.additional1());
    titleEntity.setAdditional2(titleRequest.additional2());
    titleEntity.setAdditional3(titleRequest.additional3());
    titleEntity.setAdditional4(titleRequest.additional4());
    titleEntity.setAdditional5(titleRequest.additional5());

    TitleEntity result = titleRepository.save(titleEntity);
    return titleMapper.toResponse(result);
  }
}
