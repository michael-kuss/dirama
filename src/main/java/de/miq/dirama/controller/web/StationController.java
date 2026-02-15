/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.web;

import de.miq.dirama.dto.station.StationRequest;
import de.miq.dirama.dto.station.StationResponse;
import de.miq.dirama.exception.ExistingException;
import de.miq.dirama.security.user.AuthUser;
import de.miq.dirama.service.ImageService;
import de.miq.dirama.service.StationService;
import java.util.Objects;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/stations")
public class StationController {

  private final Validator validator;
  private final StationService stationService;
  private final ImageService imageService;

  public StationController(
      StationService stationService, Validator validator, ImageService imageService) {
    this.stationService = stationService;
    this.validator = validator;
    this.imageService = imageService;
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/all")
  public String userHome(
      Model model, @PageableDefault(size = 10, sort = "name") Pageable pageable) {
    model.addAttribute("page", stationService.listAll(pageable));
    return "stations/all";
  }

  // --- NEW ---

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/new")
  public String newStationPage(@ModelAttribute StationRequest stationRequest) {
    return "stations/new";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/create")
  public String createStation(
      @RequestParam(value = "file", required = false) MultipartFile file,
      @ModelAttribute StationRequest stationRequest,
      BindingResult bindingResult) {
    validator.validate(stationRequest, bindingResult);
    if (bindingResult.hasErrors()) {
      return "stations/new";
    }
    try {
      String image = null;
      if (Objects.nonNull(file) && !file.isEmpty()) {
        image = imageService.storeImage(file);
      }

      stationService.create(stationRequest.newWithAvatarReference(image));
    } catch (ExistingException e) {
      bindingResult.rejectValue("name", "name.existing", "Stationname exists!");
      return "stations/new";
    }
    return "redirect:/stations/all";
  }

  // --- UPDATE ---

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/edit/{id}")
  public String editStationPage(@PathVariable String id, Model model) {
    StationResponse stationResponse = stationService.get(id);
    model.addAttribute("id", id);
    model.addAttribute("stationRequest", stationResponse);
    return "stations/edit";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/update/{id}")
  public String updateStation(
      @PathVariable String id,
      @RequestParam(value = "file", required = false) MultipartFile file,
      @ModelAttribute StationRequest stationRequest,
      @AuthenticationPrincipal AuthUser authUser,
      BindingResult bindingResult) {
    validator.validate(stationRequest, bindingResult);
    if (bindingResult.hasErrors()) {
      return "stations/edit";
    }

    try {
      StationRequest updateRequest = stationRequest;
      if (Objects.nonNull(file) && !file.isEmpty()) {
        updateRequest = stationRequest.newWithAvatarReference(imageService.storeImage(file));
      }

      stationService.update(id, updateRequest, authUser);
    } catch (ExistingException e) {
      bindingResult.rejectValue("name", "name.existing", "Stationname exists!");
      return "stations/edit";
    }
    return "redirect:/stations/all";
  }

  // --- DELETE ---

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/delete/{id}")
  public String deleteStation(
      @PathVariable("id") String id,
      @AuthenticationPrincipal AuthUser authUser,
      @RequestHeader("referer") String refererHeader) {
    stationService.delete(id, authUser);

    // Use referer header to redirect back, because delete can be invoked both from admin page and
    // from user stations page
    return "redirect:" + refererHeader;
  }

  // --- TOGGLE ---

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/update/{id}/toggle/status")
  public String toggleStatus(
      @PathVariable("id") String id,
      @AuthenticationPrincipal AuthUser authUser,
      @RequestHeader("referer") String refererHeader) {
    stationService.toggleStatus(id, authUser);
    return "redirect:" + refererHeader;
  }
}
