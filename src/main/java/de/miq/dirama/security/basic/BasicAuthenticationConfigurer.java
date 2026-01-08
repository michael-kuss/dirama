/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.security.basic;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthenticationConfigurer
    extends AbstractHttpConfigurer<BasicAuthenticationConfigurer, HttpSecurity> {

  private final BasicAuthenticationProvider basicAuthenticationProvider;

  private final BasicAuthenticationConverter basicAuthenticationConverter;

  private final AuthenticationEntryPoint authenticationEntryPoint;

  public BasicAuthenticationConfigurer(
      BasicAuthenticationProvider basicAuthenticationProvider,
      BasicAuthenticationConverter basicAuthenticationConverter,
      AuthenticationEntryPoint authenticationEntryPoint) {
    this.basicAuthenticationProvider = basicAuthenticationProvider;
    this.basicAuthenticationConverter = basicAuthenticationConverter;
    this.authenticationEntryPoint = authenticationEntryPoint;
  }

  @Override
  public void init(HttpSecurity http) {
    http.authenticationProvider(basicAuthenticationProvider);
  }

  @Override
  public void configure(HttpSecurity http) {

    AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

    http.addFilterBefore(
        new BasicAuthenticationFilter(
            authenticationManager, basicAuthenticationConverter, authenticationEntryPoint),
        AuthorizationFilter.class);
  }
}
