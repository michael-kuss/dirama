package de.miq.dirama.server.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(indexName = "dirama-events", type = "event")
public class Event {
    @Id
    private String id;
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "UTC")
    private Date startDate;
    private long reaccuring;
    private String event;

    private Event() {

    }

    public Event(Date startDate, long reaccuring, String event) {
        super();
        this.startDate = startDate;
        this.reaccuring = reaccuring;
        this.event = event;
    }

    public String getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public long getReaccuring() {
        return reaccuring;
    }

    public String getEvent() {
        return event;
    }
}
