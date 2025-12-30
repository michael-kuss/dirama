/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.dtos.item;

import de.miq.dirama.common.ItemState;

public record ItemResponse(String id, String data, String userId, ItemState itemState) {}
