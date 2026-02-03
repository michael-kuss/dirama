/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.web;

import de.miq.dirama.dto.user.UserCreateRequest;
import de.miq.dirama.exception.ExistingException;
import de.miq.dirama.security.user.AuthUser;
import de.miq.dirama.service.ImageService;
import de.miq.dirama.service.UserService;
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
@RequestMapping("/users")
public class UserController {
  private final Validator validator;
  private final UserService userService;
  private final ImageService imageService;

  public UserController(UserService userService, Validator validator, ImageService imageService) {
    this.userService = userService;
    this.validator = validator;
    this.imageService = imageService;
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/profile")
  public String profile(Model model, @AuthenticationPrincipal AuthUser authUser) {
    model.addAttribute("user", userService.getUserById(authUser.userId()));
    return "users/profile";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/all")
  public String userHome(Model model, @PageableDefault(size = 10, sort = "id") Pageable pageable) {
    model.addAttribute("page", userService.listAll(pageable));
    return "users/all";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/new")
  public String newEntity(@ModelAttribute UserCreateRequest userCreateRequest) {
    return "users/new";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/create")
  public String create(
      @RequestParam(value = "file", required = false) MultipartFile file,
      @ModelAttribute UserCreateRequest userCreateRequest,
      BindingResult bindingResult) {
    validator.validate(userCreateRequest, bindingResult);
    if (bindingResult.hasErrors()) {
      return "users/new";
    }
    try {
      String image = null;
      if (Objects.nonNull(file) && !file.isEmpty()) {
        image = imageService.storeImage(file);
      }

      userService.create(userCreateRequest.setAvatarReference(image));
    } catch (ExistingException e) {
      bindingResult.rejectValue("username", "username.existing", "Username exists!");
      return "users/new";
    }
    return "redirect:/users/all";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/delete/{id}")
  public String delete(
      @PathVariable String id,
      @AuthenticationPrincipal AuthUser authUser,
      @RequestHeader("referer") String refererHeader) {
    userService.delete(id, authUser);

    // Use referer header to redirect back, because delete can be invoked both from admin page and
    // from user stations page
    return "redirect:" + refererHeader;
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/update/{id}/toggle/status")
  public String toggleStatus(@PathVariable("id") String username) {
    userService.toggleStatus(username);
    return "redirect:/users/all";
  }
}
