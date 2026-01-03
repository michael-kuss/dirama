/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.security.apikey;

import de.miq.dirama.common.Role;
import de.miq.dirama.config.apikeyuser.properties.ApiKeyClientsProperties;
import de.miq.dirama.security.user.AuthUser;
import de.miq.dirama.security.user.AuthUserType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

  private final Map<String, String> apiKeysToClientIds;

  public ApiKeyAuthenticationProvider(ApiKeyClientsProperties apiKeyClientsProperties) {
    this.apiKeysToClientIds =
        apiKeyClientsProperties.getClients().entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getValue, Map.Entry::getKey, (oldValue, newValue) -> oldValue));
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    ApiKeyAuthentication apiKeyAuthentication = (ApiKeyAuthentication) authentication;

    String apiKey = apiKeyAuthentication.getCredentials();

    if (apiKey != null && apiKey.length() > 512) {
      throw new BadCredentialsException("API key is not valid1");
    }

    if (!apiKeysToClientIds.containsKey(apiKey)) {
      throw new BadCredentialsException("API key is not valid2");
    }

    String clientId = apiKeysToClientIds.get(apiKey);
    AuthUser authUser =
        AuthUser.create(
            clientId, "Application", List.of(Role.ROLE_ADMIN), null, AuthUserType.APPLICATION);
    return ApiKeyAuthentication.authenticated(authUser);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return ApiKeyAuthentication.class.isAssignableFrom(authentication);
  }
}
