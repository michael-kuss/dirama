/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.service.trigger;

import de.miq.dirama.entity.TriggerEntity;
import de.miq.dirama.exception.TriggerKeyMissingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTrigger implements TriggerInterface {
  public static final String TRIGGER_ENTITY_ID = "TRIGGER_ENTITY_ID";
  private final Map<String, String> propererties = new HashMap<>();

  public Optional<TriggerInterface> to(TriggerEntity trigger) {
    if (!trigger.getTrigger().equals(trigger())) {
      log.warn(
          "Trigger class mismatch {} {} : {}",
          this.getClass().getName(),
          this.trigger(),
          trigger.getTrigger());
      return Optional.empty();
    }

    if (!trigger.getProperties().keySet().containsAll(getMandatoryKeys())) {
      throw new TriggerKeyMissingException(
          getMandatoryKeys().stream()
              .filter(key -> !trigger.getProperties().containsKey(key))
              .toList()
              .toArray(new String[0]));
    }
    if (!trigger.isActive()) {
      return Optional.empty();
    }
    trigger.getProperties().forEach(this::setValue);
    setValue(TRIGGER_ENTITY_ID, trigger.getId());
    return Optional.of(this);
  }

  @Override
  public void setValue(String key, String value) {
    propererties.put(key, value);
  }

  @Override
  public String getValue(String key) {
    return propererties.get(key);
  }

  @Override
  public Map<String, String> getProperties() {
    return propererties;
  }
}
