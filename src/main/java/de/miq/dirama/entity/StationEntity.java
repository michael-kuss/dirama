/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "stations")
public class StationEntity {

  @Id @UuidGenerator private String id;

  @Column(unique = true)
  private String name;

  private boolean active;

  private String avatarReference;

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
