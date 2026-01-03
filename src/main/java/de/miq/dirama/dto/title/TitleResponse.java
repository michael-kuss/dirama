/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.title;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TitleResponse(
    String station,
    String artist,
    String title,
    String dabImage,
    String webImage,
    ZonedDateTime titleDate,
    String additional1,
    String additional2,
    String additional3,
    String additional4,
    String additional5) {}
