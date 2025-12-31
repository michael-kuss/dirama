/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.config;

import de.miq.dirama.common.ApiRoot;
import de.miq.dirama.security.apikey.ApiKeyAuthenticationConfigurer;
import de.miq.dirama.security.form.CustomUserDetailsService;
import de.miq.dirama.security.jwt.JwtAuthenticationConfigurer;
import de.miq.dirama.security.listener.AuthenticationManagerEventListenersConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableMethodSecurity // allow to specify access via annotations
@Configuration
public class SecurityConfig {

  private final ApiKeyAuthenticationConfigurer apiKeyAuthenticationConfigurer;

  private final JwtAuthenticationConfigurer jwtAuthenticationConfigurer;

  private final AuthenticationManagerEventListenersConfigurer
      authenticationManagerEventListenersConfigurer;

  private final AuthenticationEntryPoint authenticationEntryPoint;

  private final AccessDeniedHandler accessDeniedHandler;

  private final CustomUserDetailsService customUserDetailsService;

  public SecurityConfig(
      CustomUserDetailsService customUserDetailsService,
      ApiKeyAuthenticationConfigurer apiKeyAuthenticationConfigurer,
      JwtAuthenticationConfigurer jwtAuthenticationConfigurer,
      AuthenticationManagerEventListenersConfigurer authenticationManagerEventListenersConfigurer,
      AuthenticationEntryPoint authenticationEntryPoint,
      AccessDeniedHandler accessDeniedHandler) {
    this.apiKeyAuthenticationConfigurer = apiKeyAuthenticationConfigurer;
    this.jwtAuthenticationConfigurer = jwtAuthenticationConfigurer;
    this.authenticationManagerEventListenersConfigurer =
        authenticationManagerEventListenersConfigurer;
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.accessDeniedHandler = accessDeniedHandler;
    this.customUserDetailsService = customUserDetailsService;
  }

  @Bean
  public SecurityFilterChain webUiFilterChain(HttpSecurity http) throws Exception {

    http
        // Apply this filter chain to all requests, except requests to "/api/*"
        .securityMatcher((request) -> !request.getRequestURI().startsWith(ApiRoot.API_ROOT))
        // brings UsernamePasswordAuthenticationFilter
        .formLogin(Customizer.withDefaults())
        // brings OAuth2LoginAuthenticationFilter
        // .oauth2Login(Customizer.withDefaults())
        // allow public access to the home page
        .authorizeHttpRequests(mather -> mather.requestMatchers("/").permitAll())
        // UserDetailsService implementation to be leveraged by form login
        // UsernamePasswordAuthenticationFilter -> ProviderManager -> DaoAuthenticationProvider ->
        // CustomUserDetailsService
        .userDetailsService(customUserDetailsService)
        // allow requests to Swagger UI
        .authorizeHttpRequests(
            mather ->
                mather.requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/*",
                    "/v3/api-docs",
                    "/v3/api-docs/swagger-config"))
        .authorizeHttpRequests(matcher -> matcher.anyRequest().authenticated())
        .exceptionHandling(customizer -> customizer.accessDeniedPage("/no-access"));

    return http.build();
  }

  @Bean
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

    http.securityMatcher(ApiRoot.API_ROOT + "/**")
        .with(apiKeyAuthenticationConfigurer, Customizer.withDefaults())
        .with(jwtAuthenticationConfigurer, Customizer.withDefaults())
        .with(authenticationManagerEventListenersConfigurer, Customizer.withDefaults())
        .authorizeHttpRequests(
            matcher ->
                matcher
                    // method security will be evaluated after DSL configs,
                    // so we have to define public paths upfront
                    .requestMatchers(
                        HttpMethod.POST,
                        ApiRoot.API_ROOT + "/auth/jwt/login",
                        ApiRoot.API_ROOT + "/users")
                    .permitAll())
        .authorizeHttpRequests(matcher -> matcher.anyRequest().authenticated())
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(
            customizer ->
                customizer
                    .accessDeniedHandler(accessDeniedHandler)
                    .authenticationEntryPoint(authenticationEntryPoint));

    return http.build();
  }
}
