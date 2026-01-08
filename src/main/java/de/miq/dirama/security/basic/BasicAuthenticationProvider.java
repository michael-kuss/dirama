/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.security.basic;

import de.miq.dirama.dto.user.UserResponseWithCredentials;
import de.miq.dirama.service.UserService;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthenticationProvider implements AuthenticationProvider {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public BasicAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    UsernamePasswordAuthenticationToken basicAuthentication =
        (UsernamePasswordAuthenticationToken) authentication;

    String userName = basicAuthentication.getPrincipal().toString();

    if (userName != null && userName.length() > 128) {
      throw new BadCredentialsException("Not valid user or password!");
    }

    UserResponseWithCredentials userWithCredentials =
        userService.getUserCredentialsByUsername(userName);

    if (!passwordEncoder.matches(
        basicAuthentication.getCredentials().toString(), userWithCredentials.passwordHash())) {
      throw new BadCredentialsException("Not valid user or password!");
    }

    Set<SimpleGrantedAuthority> authorities =
        userWithCredentials.userResponse().roles().stream()
            .map(Enum::name)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

    return new UsernamePasswordAuthenticationToken(
        userWithCredentials.userResponse().username(),
        userWithCredentials.passwordHash(),
        authorities);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
