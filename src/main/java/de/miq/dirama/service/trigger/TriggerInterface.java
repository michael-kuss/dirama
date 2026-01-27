/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.service.trigger;

import de.miq.dirama.common.Triggers;
import de.miq.dirama.entity.StationEntity;
import java.util.List;
import java.util.Map;

public interface TriggerInterface {
  void execute(StationEntity station);

  void setValue(String key, String value);

  String getValue(String key);

  List<String> getMandatoryKeys();

  List<String> getOptionalKeys();

  Map<String, String> getProperties();

  Triggers trigger();
}
