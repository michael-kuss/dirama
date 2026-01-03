/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(String message, Map<String, String> details) {}
