package de.miq.dirama.server.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.miq.dirama.server.config.TriggerActionConfig;
import de.miq.dirama.server.model.Config;
import de.miq.dirama.server.repository.ConfigRepository;

@RestController
@RequestMapping(value = "/config", produces = "application/json")
public class ConfigController {
    private static final Log LOG = LogFactory.getLog(ConfigController.class);

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private TriggerActionConfig triggerConfig;

    @Value("${read.config.token}")
    private String checkToken;

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public void addConfig(@RequestBody List<Config> configs) {
        for (Config config : configs) {
            configRepository.index(config);
            LOG.info("Added " + config);
        }
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.*}")
    public void deleteConfig(@PathVariable("id") String id) {
        configRepository.delete(id);
        LOG.info("Deleted " + id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Config> requestConfig(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        if (configRepository.count() <= 0) {
            return null;
        }

        Pageable pageable = new PageRequest(page, size, new Sort(Direction.ASC,
                "key"));
        Page<Config> config = configRepository.findAll(pageable);
        if (config == null) {
            return null;
        }

        return config.getContent();
    }

    @RequestMapping(method = RequestMethod.GET, value = "stored/{token}")
    public List<Config> requestStoredConfig(@PathVariable("token") String token) {
        if (checkToken.equals("-")) {
            return null;
        }
        if (checkToken.equals(token)) {
            List<Config> ret = triggerConfig.getSettings();
            java.util.Collections.sort(ret, new Comparator<Config>() {
                @Override
                public int compare(Config o1, Config o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            });
            return ret;
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "keys")
    public List<String> requestConfigKeys(
            @RequestParam(value = "filter", defaultValue = "") String filter) {
        List<String> ret = new ArrayList<String>();

        if (configRepository.count() > 0) {
            Iterable<Config> config = configRepository.findAll();
            if (config != null) {
                Iterator<Config> it = config.iterator();
                while (it.hasNext()) {
                    Config c = it.next();
                    if (c.getKey().matches(filter)) {
                        ret.add(c.getKey());
                    }
                }
            }
        }

        List<Config> secureConfig = triggerConfig.getSettings();
        if (secureConfig != null) {
            for (Config c : secureConfig) {
                if (c.getKey().matches(filter)) {
                    ret.add(c.getKey());
                }
            }
        }
        java.util.Collections.sort(ret);
        return ret;
    }
}
