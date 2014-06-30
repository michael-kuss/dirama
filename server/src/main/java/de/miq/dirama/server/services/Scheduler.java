package de.miq.dirama.server.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Scheduler {

    @Scheduled(fixedDelay = 5000)
    public void updateTrigger() {
        //System.out.println("UPDATE!");
    }

}
