/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.web;

import de.miq.dirama.dto.station.StationRequest;
import de.miq.dirama.dto.station.StationResponse;
import de.miq.dirama.security.user.AuthUser;
import de.miq.dirama.service.StationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stations")
public class StationController {

  private final StationService stationService;

  public StationController(StationService stationService) {
    this.stationService = stationService;
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/all")
  public String userHome(Model model, Pageable pageable) {
    model.addAttribute("stations", stationService.listAllStations(pageable));
    return "stations/all";
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/admin")
  public String adminPage(Model model, Pageable pageable) {
    model.addAttribute("stations", stationService.listAllStations(pageable));
    return "stations/admin";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/new")
  public String newStationPage(Model model, StationRequest stationRequest) {
    model.addAttribute("stationRequest", stationRequest);
    return "stations/new";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/edit/{id}")
  public String editStationPage(@PathVariable("id") String stationId, Model model) {
    StationResponse stationResponse = stationService.getStation(stationId);
    model.addAttribute("stationRequest", stationResponse);
    return "stations/edit";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/create")
  public String createStation(@Valid @ModelAttribute StationRequest stationRequest) {
    stationService.createStation(stationRequest);
    return "redirect:/stations/all";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/update/{id}")
  public String updateStation(
      @PathVariable("id") String stationId,
      @AuthenticationPrincipal AuthUser authUser,
      @Valid StationRequest stationRequest) {
    stationService.updateStation(stationId, stationRequest, authUser);
    return "redirect:/stations/all";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/delete/{id}")
  public String deleteStation(
      @PathVariable("id") String stationId,
      @AuthenticationPrincipal AuthUser authUser,
      @RequestHeader("referer") String refererHeader) {
    stationService.deleteStation(stationId, authUser);

    // Use referer header to redirect back, because delete can be invoked both from admin page and
    // from user stations page
    return "redirect:" + refererHeader;
  }
}
