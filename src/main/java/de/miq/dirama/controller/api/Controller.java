/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @GetMapping(value = ApiRoot.API_ROOT + "/hello")
  public String hello() {
    return "Hello, Swagger UI!";
  }
}
