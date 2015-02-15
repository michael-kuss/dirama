package de.miq.dirama.server.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(indexName = "dirama-events", type = "event")
public class Event {
    @Id
    private String id;
    @Field(format = DateFormat.basic_date_time, pattern = "yyyyMMddHHmmss", type = FieldType.Date)
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private Date startDate;
    @Field(format = DateFormat.basic_date_time, pattern = "yyyyMMddHHmmss", type = FieldType.Date)
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private Date endDate;
    @Field(format = DateFormat.basic_date_time, pattern = "yyyyMMddHHmmss", type = FieldType.Date)
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private Date lastRun;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cron;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String event;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String station;

    public Event() {
    }

    public Event(Date startDate, Date endDate, Date lastRun, String cron,
            String event, String station) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
        this.lastRun = lastRun;
        this.cron = cron;
        this.event = event;
        this.station = station;
    }

    public String getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getLastRun() {
        return lastRun;
    }

    public void setLastRun(Date lastRun) {
        this.lastRun = lastRun;
    }

    public String getCron() {
        return cron;
    }

    public String getEvent() {
        return event;
    }

    public String getStation() {
        return station;
    }
}
