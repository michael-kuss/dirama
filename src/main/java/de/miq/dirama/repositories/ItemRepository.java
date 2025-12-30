/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.repositories;

import de.miq.dirama.enitities.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, String> {

  Page<ItemEntity> findByUserId(String userId, Pageable pageable);
}
