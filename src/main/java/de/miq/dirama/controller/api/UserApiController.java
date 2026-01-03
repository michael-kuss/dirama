/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.api;

import de.miq.dirama.common.ApiRoot;
import de.miq.dirama.common.OpenApiConstants;
import de.miq.dirama.dto.user.UserCreateRequest;
import de.miq.dirama.dto.user.UserPasswordUpdateRequest;
import de.miq.dirama.dto.user.UserResponse;
import de.miq.dirama.dto.user.UserUpdateRequest;
import de.miq.dirama.security.user.AuthUser;
import de.miq.dirama.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoot.API_ROOT + "/users")
public class UserApiController {

  private final UserService userService;

  public UserApiController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("permitAll()")
  @PostMapping
  public UserResponse registerUser(@RequestBody UserCreateRequest userCreateRequest) {

    return userService.registerUser(userCreateRequest);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/change-password")
  @SecurityRequirements({
    @SecurityRequirement(name = OpenApiConstants.BEARER_TOKEN_SECURITY_REQUIREMENT),
    @SecurityRequirement(name = OpenApiConstants.API_KEY_SECURITY_REQUIREMENT)
  })
  public void changeUserPassword(
      @AuthenticationPrincipal AuthUser authUser,
      @RequestBody UserPasswordUpdateRequest passwordUpdateRequest) {
    userService.changeUserPassword(passwordUpdateRequest, authUser);
  }

  @PreAuthorize("isAuthenticated() && #userId == authentication.principal.userId")
  @PutMapping("/{id}")
  @SecurityRequirements({
    @SecurityRequirement(name = OpenApiConstants.BEARER_TOKEN_SECURITY_REQUIREMENT),
    @SecurityRequirement(name = OpenApiConstants.API_KEY_SECURITY_REQUIREMENT)
  })
  public UserResponse updateUser(
      @PathVariable("id") String userId, @RequestBody UserUpdateRequest userUpdateRequest) {
    return userService.updateUser(userId, userUpdateRequest);
  }

  // check isAuthenticated(), because authentication.principal is String for anonymous
  // authentication
  // Spring SPEL uses the same parameter names as method parameter names.
  // Yes, there is a /me endpoint, but we also allow users to get user data by ID just for
  // demonstration purposes
  @PreAuthorize(
      "isAuthenticated() && (hasAuthority('ROLE_ADMIN') || (#userId == authentication.principal.userId))")
  @GetMapping("/{id}")
  @SecurityRequirements({
    @SecurityRequirement(name = OpenApiConstants.BEARER_TOKEN_SECURITY_REQUIREMENT),
    @SecurityRequirement(name = OpenApiConstants.API_KEY_SECURITY_REQUIREMENT)
  })
  public UserResponse getUserById(@PathVariable("id") String userId) {
    return userService.getUserById(userId);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/me")
  @SecurityRequirements({
    @SecurityRequirement(name = OpenApiConstants.BEARER_TOKEN_SECURITY_REQUIREMENT),
    @SecurityRequirement(name = OpenApiConstants.API_KEY_SECURITY_REQUIREMENT)
  })
  public UserResponse getCurrentUser(@AuthenticationPrincipal AuthUser authUser) {
    return userService.getUserById(authUser.userId());
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping
  @SecurityRequirements({
    @SecurityRequirement(name = OpenApiConstants.BEARER_TOKEN_SECURITY_REQUIREMENT),
    @SecurityRequirement(name = OpenApiConstants.API_KEY_SECURITY_REQUIREMENT)
  })
  public Page<UserResponse> listUsers(@PageableDefault @ParameterObject Pageable pageable) {
    return userService.listUsers(pageable);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/{id}/activate")
  @SecurityRequirements({
    @SecurityRequirement(name = OpenApiConstants.BEARER_TOKEN_SECURITY_REQUIREMENT),
    @SecurityRequirement(name = OpenApiConstants.API_KEY_SECURITY_REQUIREMENT)
  })
  public UserResponse activateUser(@PathVariable("id") String userId) {
    return userService.activateUser(userId);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/{id}/deactivate")
  @SecurityRequirements({
    @SecurityRequirement(name = OpenApiConstants.BEARER_TOKEN_SECURITY_REQUIREMENT),
    @SecurityRequirement(name = OpenApiConstants.API_KEY_SECURITY_REQUIREMENT)
  })
  public UserResponse deactivateUser(@PathVariable("id") String userId) {
    return userService.deactivateUser(userId);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/{id}/promote")
  @SecurityRequirements({
    @SecurityRequirement(name = OpenApiConstants.BEARER_TOKEN_SECURITY_REQUIREMENT),
    @SecurityRequirement(name = OpenApiConstants.API_KEY_SECURITY_REQUIREMENT)
  })
  public UserResponse promoteUserToAdmin(@PathVariable("id") String userId) {
    return userService.promoteUserToAdmin(userId);
  }
}
