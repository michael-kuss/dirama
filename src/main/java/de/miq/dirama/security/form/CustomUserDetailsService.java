/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.security.form;

import de.miq.dirama.dto.user.UserResponseWithCredentials;
import de.miq.dirama.security.user.AuthUser;
import de.miq.dirama.security.user.AuthUserType;
import de.miq.dirama.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserService userService;

  public CustomUserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserResponseWithCredentials userCredentialsByUsername =
        userService.getUserCredentialsByUsername(username);

    return AuthUser.create(
        userCredentialsByUsername.userResponse().id(),
        userCredentialsByUsername.userResponse().username(),
        userCredentialsByUsername.userResponse().roles(),
        userCredentialsByUsername.passwordHash(),
        AuthUserType.INTERNAL);
  }
}
