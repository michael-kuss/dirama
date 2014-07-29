package de.miq.dirama.server.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.miq.dirama.server.config.TriggerActionConfig;
import de.miq.dirama.server.model.Config;
import de.miq.dirama.server.repository.ConfigRepository;

@Service
public class ConfigService {
    private static final Log LOG = LogFactory.getLog(ConfigService.class);

    @Autowired
    private ConfigRepository config;

    @Autowired
    private TriggerActionConfig triggerConfig;

    public String getConfig(String key) {
        Config c = config.findOne(key);
        if (c != null) {
            LOG.info("Config from es <" + key + ">=<" + c.getValue() + ">");
            return c.getValue();
        }
        for (Config tc : triggerConfig.getSettings()) {
            if (tc.getKey().equals(key)) {
                LOG.info("Config from file <" + key + ">=<" + tc.getValue()
                        + ">");
                return tc.getValue();
            }
        }
        LOG.error("Config key <" + key + "> not found!");

        return null;
    }
}
