/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.trigger;

import de.miq.dirama.common.Triggers;
import java.util.Map;

public record TriggerResponse(
    String id, String station, Triggers trigger, Map<String, String> properties, boolean active) {}
