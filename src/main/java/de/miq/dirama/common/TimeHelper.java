/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeHelper {
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

  private TimeHelper() {}

  public static ZonedDateTime convert(String source) {
    if (source == null) {
      return null;
    }

    // parse to LocalDateTime
    LocalDateTime dt = LocalDateTime.parse(source, DATE_TIME_FORMATTER);
    // convert to ZonedDateTime
    return dt.atZone(ZoneId.systemDefault());
  }

  public static String convert(ZonedDateTime source) {
    if (source == null) {
      return null;
    }
    return DATE_TIME_FORMATTER.format(source);
  }
}
