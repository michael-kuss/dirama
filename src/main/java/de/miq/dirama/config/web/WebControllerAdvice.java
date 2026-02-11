/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.config.web;

import de.miq.dirama.dto.user.UserResponse;
import de.miq.dirama.security.user.AuthUser;
import de.miq.dirama.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice({"de.miq.dirama.controller.web"})
public class WebControllerAdvice {

  private final UserService userService;

  public WebControllerAdvice(UserService userService) {
    this.userService = userService;
  }

  @ModelAttribute("user")
  @PreAuthorize("isAuthenticated()")
  public UserResponse profile(@AuthenticationPrincipal AuthUser authUser) {
    return userService.getUserById(authUser.userId());
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ModelAndView handleMaxSizeException(
      MaxUploadSizeExceededException exc,
      HttpServletRequest request,
      HttpServletResponse response) {

    ModelAndView modelAndView = new ModelAndView("file");
    modelAndView.getModel().put("message", "File too large!");
    return modelAndView;
  }
}
