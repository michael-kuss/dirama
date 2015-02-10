package de.miq.dirama.server.services;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import de.miq.dirama.server.model.Event;
import de.miq.dirama.server.model.Title;
import de.miq.dirama.server.model.Trigger;
import de.miq.dirama.server.repository.EventRepository;
import de.miq.dirama.server.repository.TitleRepository;

@Service
public class Scheduler {
    private static final Log LOG = LogFactory.getLog(Scheduler.class);

    @Autowired
    private EventRepository events;

    @Autowired
    private TriggerActionService actionService;

    @Autowired
    private TitleRepository titleRepository;

    private boolean firstRun = true;

    @Scheduled(fixedDelay = 5000)
    public void updateTrigger() {
        try {
            Date now = new Date();
            if (events.count() > 0) {
                for (Event event : events.findAll()) {
                    if (event.getStartDate().before(now)
                            && event.getEndDate().after(now)) {
                        CronSequenceGenerator cron = new CronSequenceGenerator(
                                event.getCron());
                        Date check = event.getStartDate();
                        if (event.getLastRun() != null) {
                            check = event.getLastRun();
                        }

                        if (cron.next(check).before(now)) {
                            if (!firstRun) {
                                LOG.info("doing cron <" + event.getId() + "> <"
                                        + event.getEvent() + ">");

                                Trigger trigger = new Trigger("event-"
                                        + event.getId(), "true", event.getEvent());

                                Pageable pageable = new PageRequest(0, 1,
                                        new Sort(Direction.DESC, "time"));
                                Page<Title> titles = titleRepository
                                        .findByStation(event.getStation(),
                                                pageable);
                                Title title = null;
                                if (titles != null
                                        && titles.getTotalElements() > 0) {
                                    title = titles.iterator().next();
                                }

                                actionService.executeTrigger(trigger, title);
                            }

                            event.setLastRun(now);
                            events.save(event);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // ignore
            LOG.error(e.getMessage(), e);
        }
        firstRun = false;
    }

    public static void main(String[] args) {
        CronSequenceGenerator cron = new CronSequenceGenerator("*/10 * * * * *");
        Date now = new Date();
        System.out.println(now + " -- " + cron.next(now));
    }
}
