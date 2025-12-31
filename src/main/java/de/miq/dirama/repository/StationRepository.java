/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.repository;

import de.miq.dirama.entity.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<StationEntity, String> {}
