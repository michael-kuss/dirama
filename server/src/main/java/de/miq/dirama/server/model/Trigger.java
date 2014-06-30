package de.miq.dirama.server.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "dirama-trigger", type = "trigger")
public class Trigger {
    @Id
    private String id;
    private String cause;
    private String action;

    private Trigger() {

    }

    public Trigger(String id, String cause, String action) {
        super();
        this.id = id;
        this.cause = cause;
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public String getCause() {
        return cause;
    }

    public String getAction() {
        return action;
    }
}
