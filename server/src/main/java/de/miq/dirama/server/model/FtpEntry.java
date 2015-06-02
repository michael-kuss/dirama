package de.miq.dirama.server.model;

import java.util.Date;

public class FtpEntry {
    private String name;
    private long size;
    private Date timestamp;
    private int type;

    public FtpEntry() {
    }

    public FtpEntry(String name, long size, Date timestamp, int type) {
        super();
        this.name = name;
        this.size = size;
        this.timestamp = timestamp;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getType() {
        return type;
    }

}
