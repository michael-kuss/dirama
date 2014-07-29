package de.miq.dirama.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.miq.dirama.server.repository.EventRepository;

@Service
public class Scheduler {

    @Autowired
    private EventRepository events;

    @Scheduled(fixedDelay = 5000)
    public void updateTrigger() {
        // System.out.println("UPDATE!");
    }
}
