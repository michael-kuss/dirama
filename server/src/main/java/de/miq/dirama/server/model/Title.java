package de.miq.dirama.server.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(indexName = "dirama-titles", type = "title")
public class Title {
    @Id
    private String id;
    private String station;
    private String artist;
    private String title;
    private String dabImage;
    private String webImage;
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "UTC")
    private Date time;

    public Title() {
    }

    public Title(String station, String artist, String title, String dabImage,
            String webImage, Date time) {
        super();
        this.station = station;
        this.artist = artist;
        this.title = title;
        this.dabImage = dabImage;
        this.webImage = webImage;
        this.time = time;
    }

    public String getStation() {
        return station;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getDabImage() {
        return dabImage;
    }

    public String getWebImage() {
        return webImage;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();

        buf.append(station);
        buf.append(";");
        buf.append(artist);
        buf.append(";");
        buf.append(title);
        buf.append(";");
        buf.append(SimpleDateFormat.getDateTimeInstance().format(time));
        buf.append(";");
        buf.append(dabImage);
        buf.append(";");
        buf.append(webImage);

        return buf.toString();
    }
}
