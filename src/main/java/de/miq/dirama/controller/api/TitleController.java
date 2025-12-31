/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.controller.api;

import de.miq.dirama.common.ApiRoot;
import de.miq.dirama.common.OpenApiConstants;
import de.miq.dirama.dto.title.TitleRequest;
import de.miq.dirama.dto.title.TitleResponse;
import de.miq.dirama.service.TitleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoot.API_ROOT + "/title")
@SecurityRequirements({
  @SecurityRequirement(name = OpenApiConstants.BEARER_TOKEN_SECURITY_REQUIREMENT),
  @SecurityRequirement(name = OpenApiConstants.API_KEY_SECURITY_REQUIREMENT)
})
public class TitleController {

  private final TitleService titleService;

  public TitleController(TitleService titleService) {
    this.titleService = titleService;
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping(value = {"/{station}"})
  public TitleResponse createTitle(
      @PathVariable String station, @RequestBody TitleRequest titleRequest) {
    return titleService.createTitle(station, titleRequest);
  }
}
