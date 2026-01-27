/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.repository;

import de.miq.dirama.entity.StationEntity;
import de.miq.dirama.entity.TitleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleRepository extends JpaRepository<TitleEntity, String> {
  Page<TitleEntity> findByStationOrderByTitleDateDesc(StationEntity station, Pageable pageable);
}
