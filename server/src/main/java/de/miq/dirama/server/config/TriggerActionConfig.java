package de.miq.dirama.server.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "trigger")
public class TriggerActionConfig {
    private List<TriggerProperty> settings;

    public List<TriggerProperty> getSettings() {
        return settings;
    }

    public void setSettings(List<TriggerProperty> settings) {
        this.settings = settings;
    }

    public static class TriggerProperty {        
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
