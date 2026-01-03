/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.api;

import de.miq.dirama.common.ApiRoot;
import de.miq.dirama.common.OpenApiConstants;
import de.miq.dirama.dto.station.StationRequest;
import de.miq.dirama.dto.station.StationResponse;
import de.miq.dirama.security.user.AuthUser;
import de.miq.dirama.service.StationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoot.API_ROOT + "/stations")
@SecurityRequirements({
  @SecurityRequirement(name = OpenApiConstants.BEARER_TOKEN_SECURITY_REQUIREMENT),
  @SecurityRequirement(name = OpenApiConstants.API_KEY_SECURITY_REQUIREMENT)
})
public class StationApiController {

  private final StationService stationService;

  public StationApiController(StationService stationService) {
    this.stationService = stationService;
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping
  public StationResponse createStation(@Valid @RequestBody StationRequest stationRequest) {
    return stationService.createStation(stationRequest);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/{id}")
  public StationResponse getStation(@PathVariable("id") String stationId) {
    return stationService.getStation(stationId);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping
  public Page<StationResponse> listAllStations(
      @PageableDefault @ParameterObject Pageable pageable) {
    return stationService.listAllStations(pageable);
  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping("/{id}")
  public StationResponse updateStation(
      @PathVariable("id") String stationId,
      @Valid @RequestBody StationRequest stationRequest,
      @AuthenticationPrincipal AuthUser authUser) {
    return stationService.updateStation(stationId, stationRequest, authUser);
  }

  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/{id}")
  public void deleteStation(
      @PathVariable("id") String stationId, @AuthenticationPrincipal AuthUser authUser) {
    stationService.deleteStation(stationId, authUser);
  }
}
