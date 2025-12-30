/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.enitities;

import de.miq.dirama.common.ItemState;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "item")
public class ItemEntity {

  @Id @UuidGenerator private String id;

  private String data;

  private String userId;

  private ItemState itemState;

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
