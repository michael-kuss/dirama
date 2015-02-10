package de.miq.dirama.server.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Document(indexName = "dirama-titles", type = "title")
public class Title {
    @Id
    private String id;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String station;
    private String artist;
    private String title;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String dabImage;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String webImage;
    @CreatedDate
    @Field(format = DateFormat.basic_date_time, pattern = "yyyyMMddHHmmss", type = FieldType.Date)
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private Date time;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String additional1;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String additional2;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String additional3;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String additional4;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String additional5;
    @Field(type = FieldType.Nested)
    private Map<String, String> properties;

    public Title() {
    }

    public Title(String station, String artist, String title, String dabImage,
            String webImage, Date time, String additional1, String additional2,
            String additional3, String additional4, String additional5) {
        super();
        this.station = station;
        this.artist = artist;
        this.title = title;
        this.dabImage = dabImage;
        this.webImage = webImage;
        this.time = time;
        this.additional1 = additional1;
        this.additional2 = additional2;
        this.additional3 = additional3;
        this.additional4 = additional4;
        this.additional5 = additional5;
        if (station != null) {
            this.id = (station + artist + title + time).hashCode() + "";
        }
    }

    public Title(String station, Title title) {
        this(station, title.getArtist(), title.getTitle(), title.getDabImage(),
                title.getWebImage(), title.getTime(), title.getAdditional1(),
                title.getAdditional2(), title.getAdditional3(), title
                        .getAdditional4(), title.getAdditional5());
    }

    public String getId() {
        return id;
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

    public String getAdditional1() {
        return additional1;
    }

    public String getAdditional2() {
        return additional2;
    }

    public String getAdditional3() {
        return additional3;
    }

    public String getAdditional4() {
        return additional4;
    }

    public String getAdditional5() {
        return additional5;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void addProperty(String key, String value) {
        if (this.properties == null) {
            this.properties = new HashMap<String, String>();
        }
        this.properties.put(key, value);
    }

    public String getProperty(String key) {
        if (this.properties == null) {
            return null;
        }
        return this.properties.get(key);
    }

    public void setStation(String station) {
        this.station = station;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDabImage(String dabImage) {
        this.dabImage = dabImage;
    }

    public void setWebImage(String webImage) {
        this.webImage = webImage;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setAdditional1(String additional1) {
        this.additional1 = additional1;
    }

    public void setAdditional2(String additional2) {
        this.additional2 = additional2;
    }

    public void setAdditional3(String additional3) {
        this.additional3 = additional3;
    }

    public void setAdditional4(String additional4) {
        this.additional4 = additional4;
    }

    public void setAdditional5(String additional5) {
        this.additional5 = additional5;
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
