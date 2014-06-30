package de.miq.dirama.server.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.miq.dirama.server.model.Trigger;
import de.miq.dirama.server.repository.TriggerRepository;

@Controller
@RequestMapping("/trigger")
public class TriggerController {
    private static final Log LOG = LogFactory.getLog(TriggerController.class);

    @Autowired
    private TriggerRepository triggerRepository;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<String>> addTrigger(
            @RequestBody() Trigger trigger) {
        // Trigger trigger = new Trigger(cause, action);

        triggerRepository.index(trigger);
        LOG.info("Added " + trigger);

        return null;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseBody
    public ResponseEntity<List<String>> addTrigger(@PathVariable("id") String id) {
        triggerRepository.delete(id);
        LOG.info("Deleted Trigger " + id);

        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Trigger> requestTrigger(
            @RequestParam(value = "size", defaultValue = "50") int size) {
        Pageable pageable = new PageRequest(0, size);
        Page<Trigger> trigger = triggerRepository.findAll(pageable);
        if (trigger == null) {
            return null;
        }

        return trigger.getContent();
    }
}
