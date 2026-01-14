/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.repository;

import de.miq.dirama.entity.StationEntity;
import de.miq.dirama.entity.TriggerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TriggerRepository extends JpaRepository<TriggerEntity, String> {
  Page<TriggerEntity> findAllByStation(StationEntity station, Pageable pageable);
}
