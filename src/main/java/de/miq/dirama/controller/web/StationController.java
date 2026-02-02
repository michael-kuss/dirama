/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.web;

import de.miq.dirama.dto.station.StationRequest;
import de.miq.dirama.dto.station.StationResponse;
import de.miq.dirama.exception.ExistingException;
import de.miq.dirama.security.user.AuthUser;
import de.miq.dirama.service.StationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stations")
public class StationController {

  private final Validator validator;
  private final StationService stationService;

  public StationController(StationService stationService, Validator validator) {
    this.stationService = stationService;
    this.validator = validator;
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/all")
  public String userHome(
      Model model, @PageableDefault(size = 10, sort = "name") Pageable pageable) {
    model.addAttribute("page", stationService.listAll(pageable));
    return "stations/all";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/new")
  public String newStationPage(@ModelAttribute StationRequest stationRequest) {
    return "stations/new";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/edit/{id}")
  public String editStationPage(@PathVariable("id") String stationId, Model model) {
    StationResponse stationResponse = stationService.get(stationId);
    model.addAttribute("stationRequest", stationResponse);
    return "stations/edit";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/create")
  public String createStation(
      @ModelAttribute StationRequest stationRequest, BindingResult bindingResult) {
    validator.validate(stationRequest, bindingResult);
    if (bindingResult.hasErrors()) {
      return "stations/new";
    }
    try {
      stationService.create(stationRequest);
    } catch (ExistingException e) {
      bindingResult.rejectValue("name", "name.existing", "Name exists!");
      return "stations/new";
    }
    return "redirect:/stations/all";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/update/{id}")
  public String updateStation(
      @PathVariable("id") String stationId,
      @AuthenticationPrincipal AuthUser authUser,
      @Valid StationRequest stationRequest) {
    stationService.update(stationId, stationRequest, authUser);
    return "redirect:/stations/all";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/delete/{name}")
  public String deleteStation(
      @PathVariable("name") String stationName,
      @AuthenticationPrincipal AuthUser authUser,
      @RequestHeader("referer") String refererHeader) {
    stationService.delete(stationName, authUser);

    // Use referer header to redirect back, because delete can be invoked both from admin page and
    // from user stations page
    return "redirect:" + refererHeader;
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/update/{id}/toggle/status")
  public String toggleStatus(
      @PathVariable("id") String id, @RequestHeader("referer") String refererHeader) {
    stationService.toggleStatus(id);
    return "redirect:" + refererHeader;
  }
}
