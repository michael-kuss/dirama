/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.api;

import de.miq.dirama.common.ApiRoot;
import de.miq.dirama.common.OpenApiConstants;
import de.miq.dirama.dto.title.TitleRequest;
import de.miq.dirama.dto.title.TitleResponse;
import de.miq.dirama.service.TitleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoot.API_ROOT + "/title")
@SecurityRequirements({
  @SecurityRequirement(name = OpenApiConstants.BEARER_TOKEN_SECURITY_REQUIREMENT),
  @SecurityRequirement(name = OpenApiConstants.API_KEY_SECURITY_REQUIREMENT)
})
@Validated
public class TitleController {

  private final TitleService titleService;

  public TitleController(TitleService titleService) {
    this.titleService = titleService;
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping(value = {"/{station}"})
  public TitleResponse createTitle(
      @NotNull @PathVariable String station,
      @Valid @RequestBody TitleRequest titleRequest,
      @RequestParam(value = "ignoreNow", defaultValue = "false") boolean ignoreNow,
      @RequestParam(value = "trigger", defaultValue = "true") boolean trigger) {
    return titleService.createTitle(station, titleRequest, ignoreNow, trigger);
  }
}
