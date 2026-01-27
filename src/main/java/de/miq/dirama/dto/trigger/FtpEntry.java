/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.dto.trigger;

import java.util.Date;

public record FtpEntry(String name, long size, Date timestamp, int type) {}
