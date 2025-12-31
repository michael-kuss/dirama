/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.security.exceptions.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.miq.dirama.dto.error.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  public ApplicationAccessDeniedHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {

    log.error("Access denied for request: {}", request, accessDeniedException);

    ApiErrorResponse apiErrorResponse = new ApiErrorResponse(accessDeniedException.getMessage());

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json");
    objectMapper.writeValue(response.getOutputStream(), apiErrorResponse);
  }
}
