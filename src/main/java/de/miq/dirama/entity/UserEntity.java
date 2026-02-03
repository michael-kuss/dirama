/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.entity;

import de.miq.dirama.common.Role;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
  @Id @UuidGenerator private String id;

  @Column(unique = true)
  private String username;

  private String passwordHash;

  private String firstName;

  private String lastName;

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.EAGER)
  private Set<Role> roles;

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
