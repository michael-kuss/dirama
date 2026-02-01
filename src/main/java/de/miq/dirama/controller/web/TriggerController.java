/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.web;

import de.miq.dirama.service.TriggerService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/triggers")
public class TriggerController {
  private final TriggerService triggerService;

  public TriggerController(TriggerService triggerService) {
    this.triggerService = triggerService;
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/all")
  public String userHome(Model model, Pageable pageable) {
    model.addAttribute("triggers", triggerService.listAll(pageable));
    return "triggers/all";
  }
}
