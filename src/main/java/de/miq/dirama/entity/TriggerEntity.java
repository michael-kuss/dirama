/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.entity;

import de.miq.dirama.common.Triggers;
import de.miq.dirama.config.hibernate.PropertiesAttributeConverter;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.Map;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "triggers")
public class TriggerEntity {
  @Id @UuidGenerator private String id;

  @ManyToOne @PrimaryKeyJoinColumn private StationEntity station;

  private Triggers trigger;

  @Convert(converter = PropertiesAttributeConverter.class)
  @Column(name = "properties", length = 4096)
  private Map<String, String> properties;

  private boolean active;

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
