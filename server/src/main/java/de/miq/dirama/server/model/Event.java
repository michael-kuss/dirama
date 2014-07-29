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
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private long reaccuring;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String event;

    public Event() {
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
