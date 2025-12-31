/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.dto.title;

import java.time.ZonedDateTime;

public record TitleRequest(
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
