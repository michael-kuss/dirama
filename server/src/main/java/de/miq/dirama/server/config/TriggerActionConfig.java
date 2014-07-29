package de.miq.dirama.server.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import de.miq.dirama.server.model.Config;

@Configuration
@ConfigurationProperties(prefix = "trigger")
public class TriggerActionConfig {
    private List<Config> settings;

    public List<Config> getSettings() {
        return settings;
    }

    public void setSettings(List<Config> settings) {
        this.settings = settings;
    }
}
