/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.web;

import de.miq.dirama.service.TriggerService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/triggers")
public class TriggerController {
  private final TriggerService triggerService;

  public TriggerController(TriggerService triggerService) {
    this.triggerService = triggerService;
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/all")
  public String userHome(Model model, @PageableDefault(size = 10, sort = "id") Pageable pageable) {
    model.addAttribute("page", triggerService.listAll(pageable));
    return "triggers/all";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/update/{id}/toggle/status")
  public String toggleStatus(@PathVariable("id") String id) {
    triggerService.toggleStatus(id);
    return "redirect:/triggers/all";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/delete/{id}")
  public String delete(@PathVariable("id") String id) {
    triggerService.delete(id);
    return "redirect:/triggers/all";
  }
}
