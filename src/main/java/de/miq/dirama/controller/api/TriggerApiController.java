/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.api;

import de.miq.dirama.common.ApiRoot;
import de.miq.dirama.common.OpenApiConstants;
import de.miq.dirama.dto.trigger.TriggerRequest;
import de.miq.dirama.dto.trigger.TriggerResponse;
import de.miq.dirama.service.TriggerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoot.API_ROOT + "/trigger")
@SecurityRequirements({
  @SecurityRequirement(name = OpenApiConstants.BEARER_TOKEN_SECURITY_REQUIREMENT),
  @SecurityRequirement(name = OpenApiConstants.API_KEY_SECURITY_REQUIREMENT)
})
@Validated
public class TriggerApiController {

  private final TriggerService triggerService;

  public TriggerApiController(TriggerService triggerService) {
    this.triggerService = triggerService;
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping(value = {"/{station}"})
  public TriggerResponse createTrigger(
      @NotNull @PathVariable String station, @Valid @RequestBody TriggerRequest triggerRequest) {
    return triggerService.create(station, triggerRequest);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping
  public Page<TriggerResponse> listAll(@PageableDefault @ParameterObject Pageable pageable) {
    return triggerService.listAll(pageable);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = {"/{station}"})
  public Page<TriggerResponse> listAllTitlesByStation(
      @NotNull @PathVariable String station, @PageableDefault @ParameterObject Pageable pageable) {
    return triggerService.listAllByStation(station, pageable);
  }
}
