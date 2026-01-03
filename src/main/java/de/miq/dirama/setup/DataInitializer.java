/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.setup;

import de.miq.dirama.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class DataInitializer {
  @Autowired private UserService userService;

  @EventListener(ApplicationReadyEvent.class)
  public void createDefaultAdmin() {
    this.userService.createDefaultAdminIfNotExist();
  }
}
