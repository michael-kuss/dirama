/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.repository;

import de.miq.dirama.entity.StationEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<StationEntity, String> {
  Optional<StationEntity> findByName(String stationName);
}
