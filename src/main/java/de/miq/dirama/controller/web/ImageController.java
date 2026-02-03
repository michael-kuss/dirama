/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.web;

import de.miq.dirama.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/avatars")
public class ImageController {
  private final ImageService imageService;

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/{name}")
  public ResponseEntity<byte[]> getImage(@PathVariable String name) {
    return imageService.getImageAsByteArray(imageService.getImagePath(name));
  }
}
