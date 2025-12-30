/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.security.oauth;

import de.miq.dirama.dtos.user.UserResponse;
import de.miq.dirama.dtos.user.UserSyncRequest;
import de.miq.dirama.security.user.AuthUser;
import de.miq.dirama.security.user.AuthUserType;
import de.miq.dirama.services.UserService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOauthUserService extends DefaultOAuth2UserService {

  private final UserService userService;

  public CustomOauthUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2User auth2User = super.loadUser(userRequest);

    Integer userId = auth2User.getAttribute("id");
    String username = auth2User.getAttribute("login");
    String name = auth2User.getAttribute("name");

    Pair<String, String> firstAndLastNames = toFirstAndLastName(name);

    UserResponse userResponse =
        userService.syncUser(
            new UserSyncRequest(
                String.valueOf(userId),
                username,
                firstAndLastNames.getLeft(),
                firstAndLastNames.getRight()));

    return AuthUser.create(
        userResponse.id(), username, userResponse.roles(), null, AuthUserType.OAUTH);
  }

  private Pair<String, String> toFirstAndLastName(String name) {

    int firstSpaceIndex = name.indexOf(' ');

    if (firstSpaceIndex == -1) {
      return Pair.of(name, "");
    } else {
      String firstName = name.substring(0, firstSpaceIndex);
      String lastName = name.substring(firstSpaceIndex + 1);
      return Pair.of(firstName, lastName);
    }
  }
}
