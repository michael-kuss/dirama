/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.security.jwt;

import de.miq.dirama.dto.user.UserResponse;
import de.miq.dirama.dto.user.UserResponseWithCredentials;
import de.miq.dirama.security.exceptions.ApplicationAuthenticationException;
import de.miq.dirama.security.jwt.dto.LoginDto;
import de.miq.dirama.security.jwt.dto.TokenDto;
import de.miq.dirama.security.user.AuthUser;
import de.miq.dirama.security.user.AuthUserType;
import de.miq.dirama.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthService {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  private final JwtService jwtService;

  public JwtAuthService(
      UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public TokenDto login(LoginDto loginDto) {

    UserResponseWithCredentials userCredentials =
        userService.getUserCredentialsByUsername(loginDto.username());

    if (!passwordEncoder.matches(loginDto.password(), userCredentials.passwordHash())) {
      throw new ApplicationAuthenticationException("Password is incorrect");
    }

    UserResponse userResponse = userCredentials.userResponse();
    AuthUser authUser =
        AuthUser.create(
            userResponse.id(),
            userResponse.username(),
            userResponse.roles(),
            null,
            AuthUserType.INTERNAL);

    String jwtToken = jwtService.createJwtToken(authUser);

    return new TokenDto(jwtToken);
  }
}
