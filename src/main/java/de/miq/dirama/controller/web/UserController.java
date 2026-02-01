/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.web;

import de.miq.dirama.security.user.AuthUser;
import de.miq.dirama.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/profile")
  public String profile(Model model, @AuthenticationPrincipal AuthUser authUser) {
    model.addAttribute("pageTitle", "UserProfile");
    model.addAttribute("user", userService.getUserById(authUser.userId()));
    return "users/profile";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/all")
  public String userHome(Model model, Pageable pageable) {
    model.addAttribute("users", userService.listAll(pageable));
    return "users/all";
  }
}
