/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.controller.api;

import de.miq.dirama.security.jwt.JwtAuthService;
import de.miq.dirama.security.jwt.dto.LoginDto;
import de.miq.dirama.security.jwt.dto.TokenDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/jwt")
public class JwtAuthApiController {

  private final JwtAuthService jwtAuthService;

  public JwtAuthApiController(JwtAuthService jwtAuthService) {
    this.jwtAuthService = jwtAuthService;
  }

  @PreAuthorize("isAnonymous()")
  @PostMapping("/login")
  public TokenDto login(@RequestBody LoginDto loginDto) {

    return jwtAuthService.login(loginDto);
  }
}
