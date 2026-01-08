/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.title;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public record TitleRequest(
    @NotNull String artist,
    @NotNull String title,
    @NotNull String dabImage,
    @NotNull String webImage,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss", timezone = "DEFAULT_TIMEZONE")
        @NotNull ZonedDateTime titleDate,
    String additional1,
    String additional2,
    String additional3,
    String additional4,
    String additional5) {}
