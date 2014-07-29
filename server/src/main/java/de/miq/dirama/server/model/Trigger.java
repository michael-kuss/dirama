package de.miq.dirama.server.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "dirama-triggers", type = "trigger")
public class Trigger {
    @Id
    private String id;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cause;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String action;

    public Trigger() {
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
