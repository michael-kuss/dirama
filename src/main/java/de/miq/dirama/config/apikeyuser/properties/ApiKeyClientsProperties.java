/**
 * Copyright (c) 2015-2025 Michael Kuß
 */
package de.miq.dirama.config.apikeyuser.properties;

import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api-key")
@Data
public class ApiKeyClientsProperties {

  private Map<String, String> clients;
}
