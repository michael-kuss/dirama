/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.service.trigger;

import de.miq.dirama.common.Triggers;
import de.miq.dirama.entity.StationEntity;
import de.miq.dirama.entity.TitleEntity;
import de.miq.dirama.repository.TitleRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Slf4j
public class LogTrigger extends AbstractTrigger {

  private final TitleRepository titleRepository;

  public LogTrigger(TitleRepository titleRepository) {
    this.titleRepository = titleRepository;
  }

  public static String TAIL = "tail";

  private final List<String> MANDATORY_KEYS = List.of();
  private final List<String> OPTIONA_KEYS = Collections.singletonList(TAIL);

  @Override
  public void execute(StationEntity station) {
    Pageable pageable =
        PageRequest.of(
            0,
            Integer.parseInt(Optional.ofNullable(getValue(TAIL)).orElse("1")),
            Sort.by(Sort.Direction.DESC, "titleDate"));
    List<TitleEntity> titles =
        titleRepository.findByStationOrderByTitleDateDesc(station, pageable).getContent();
    log.info("{} : {}", getValue(TRIGGER_ENTITY_ID), titles);
  }

  @Override
  public List<String> getMandatoryKeys() {
    return MANDATORY_KEYS;
  }

  @Override
  public List<String> getOptionalKeys() {
    return OPTIONA_KEYS;
  }

  @Override
  public Triggers trigger() {
    return Triggers.LOG;
  }
}
