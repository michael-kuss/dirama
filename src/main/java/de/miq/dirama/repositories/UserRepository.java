/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.repositories;

import de.miq.dirama.enitities.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, String> {
  Optional<UserEntity> findByUsername(String username);

  @Query(
      """
      SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u
          WHERE de.miq.dirama.common.Role.ROLE_ADMIN MEMBER OF u.roles
      """)
  boolean isAnyAdminExist();
}
