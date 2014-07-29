package de.miq.dirama.server.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import de.miq.dirama.server.model.Title;
import de.miq.dirama.server.model.Trigger;
import de.miq.dirama.server.repository.TriggerRepository;

@Service
public class TriggersActionService {
    private static final Log LOG = LogFactory
            .getLog(TriggersActionService.class);

    @Autowired
    private TriggerActionService actionService;

    @Autowired
    private TriggerRepository triggerRepository;

    @Async
    public void executeTriggers(Title title) {
        Iterable<Trigger> triggers = triggerRepository.findAll();

        LOG.info("triggers...");
        for (Trigger trigger : triggers) {
            actionService.executeTrigger(trigger, title);
        }
    }

}
