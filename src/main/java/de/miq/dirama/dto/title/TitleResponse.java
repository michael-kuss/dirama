/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.title;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;

public record TitleResponse(
    String id,
    String station,
    String artist,
    String title,
    String dabImage,
    String webImage,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        ZonedDateTime titleDate,
    String additional1,
    String additional2,
    String additional3,
    String additional4,
    String additional5) {}
