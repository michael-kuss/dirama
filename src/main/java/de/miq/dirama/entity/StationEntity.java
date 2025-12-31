/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.entity;

import de.miq.dirama.common.StationState;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "stations")
public class StationEntity {

  @Id @UuidGenerator private String id;

  private String name;

  private StationState stationState;

  private ZonedDateTime createdDate;

  private ZonedDateTime updatedDate;

  @PrePersist
  public void onPrePersist() {

    createdDate = ZonedDateTime.now();
    updatedDate = ZonedDateTime.now();
  }

  @PreUpdate
  public void onPreUpdate() {

    updatedDate = ZonedDateTime.now();
  }
}
