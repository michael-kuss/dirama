/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.trigger;

import de.miq.dirama.common.Triggers;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record TriggerRequest(
    @NotNull Triggers trigger, Map<String, String> properties, boolean active) {}
