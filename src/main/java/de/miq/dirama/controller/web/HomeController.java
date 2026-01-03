/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping
  public String homePage() {
    return "home";
  }
}
