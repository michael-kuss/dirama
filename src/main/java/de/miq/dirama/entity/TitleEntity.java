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
@Table(name = "titles")
public class TitleEntity {
  @Id @UuidGenerator private String id;

  @OneToOne @PrimaryKeyJoinColumn private StationEntity station;

  private String artist;
  private String title;
  private String dabImage;
  private String webImage;
  private ZonedDateTime titleDate;
  private String additional1;
  private String additional2;
  private String additional3;
  private String additional4;
  private String additional5;
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
