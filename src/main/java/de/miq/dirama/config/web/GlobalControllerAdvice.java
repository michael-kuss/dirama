/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.config.web;

import de.miq.dirama.dto.user.UserResponse;
import de.miq.dirama.security.user.AuthUser;
import de.miq.dirama.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "de.miq.dirama.controller.web")
public class GlobalControllerAdvice {

  private final UserService userService;

  public GlobalControllerAdvice(UserService userService) {
    this.userService = userService;
  }

  @ModelAttribute("user")
  @PreAuthorize("isAuthenticated()")
  public UserResponse profile(@AuthenticationPrincipal AuthUser authUser) {
    return userService.getUserById(authUser.userId());
  }
}
