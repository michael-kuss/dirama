/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.config.hibernate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class PropertiesAttributeConverter
    implements AttributeConverter<Map<String, String>, String> {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Map<String, String> properties) {
    try {
      return objectMapper.writeValueAsString(properties);
    } catch (JsonProcessingException jpe) {
      log.warn("Cannot convert Address into JSON");
      return null;
    }
  }

  @Override
  public Map<String, String> convertToEntityAttribute(String value) {
    try {
      return objectMapper.readValue(value, Map.class);
    } catch (JsonProcessingException e) {
      log.warn("Cannot convert JSON into Address");
      return null;
    }
  }
}
