/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.web;

import de.miq.dirama.service.StationService;
import de.miq.dirama.service.TitleService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  private final TitleService titleService;
  private final StationService stationService;

  public HomeController(TitleService titleService, StationService stationService) {
    this.titleService = titleService;
    this.stationService = stationService;
  }

  @GetMapping
  public String homePage(
      Model model, @PageableDefault(size = 10, sort = "titleDate") Pageable pageable) {
    model.addAttribute("page", titleService.listAll(pageable));

    return "home";
  }
}
