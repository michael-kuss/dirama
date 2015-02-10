package de.miq.dirama.server.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.miq.dirama.server.model.Title;
import de.miq.dirama.server.model.Trigger;
import de.miq.dirama.server.repository.TitleRepository;
import de.miq.dirama.server.repository.TriggerRepository;
import de.miq.dirama.server.services.TriggerActionService;

@RestController
@RequestMapping(value = "/trigger", produces = "application/json")
public class TriggerController {
    private static final Log LOG = LogFactory.getLog(TriggerController.class);

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private TriggerActionService triggerService;

    @Autowired
    private TitleRepository titleRepository;

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public void addTrigger(@RequestBody List<Trigger> triggers) {
        for (Trigger trigger : triggers) {
            triggerRepository.index(trigger);
            LOG.info("Added " + trigger);
        }
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteTrigger(@PathVariable("id") String id) {
        triggerRepository.delete(id);
        LOG.info("Deleted Trigger " + id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Trigger> requestTrigger(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        if (triggerRepository.count() <= 0) {
            return null;
        }
        Pageable pageable = new PageRequest(page, size, new Sort(Direction.ASC,
                "id"));
        Page<Trigger> trigger = triggerRepository.findAll(pageable);
        if (trigger == null) {
            return null;
        }

        return trigger.getContent();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/{station}")
    public void doTrigger(@PathVariable("id") String id,
            @PathVariable("station") String station) {
        Trigger trigger = triggerRepository.findOne(id);
        Pageable pageable = new PageRequest(0, 1, new Sort(Direction.DESC,
                "time"));
        Page<Title> titles = titleRepository.findByStation(station, pageable);
        if (titles == null || trigger == null
                || titles.getContent().size() == 0) {
            return;
        }
        Title now = titles.getContent().get(0);
        triggerService.executeTrigger(trigger, now);
    }
}
