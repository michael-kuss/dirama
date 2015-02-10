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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.miq.dirama.server.model.Event;
import de.miq.dirama.server.repository.EventRepository;

@RestController
@RequestMapping(value = "/event", produces = "application/json")
public class EventController {
    private static final Log LOG = LogFactory.getLog(EventController.class);

    @Autowired
    private EventRepository eventRepository;

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public void addConfig(@RequestBody List<Event> events) {
        for (Event event : events) {
            eventRepository.index(event);
            LOG.info("Added " + event);
        }
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.*}")
    public void deleteConfig(@PathVariable("id") String id) {
        eventRepository.delete(id);
        LOG.info("Deleted " + id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Event> requestEvent(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        if (eventRepository.count() <= 0) {
            return null;
        }

        Pageable pageable = new PageRequest(page, size, new Sort(Direction.ASC,
                "id"));
        Page<Event> events = eventRepository.findAll(pageable);
        if (events == null) {
            return null;
        }

        return events.getContent();
    }
}
