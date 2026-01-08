/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import de.miq.dirama.common.TimeHelper;
import java.io.IOException;
import java.time.ZonedDateTime;

public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

  @Override
  public ZonedDateTime deserialize(
      JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

    return TimeHelper.convert(jsonParser.getText());
  }
}
