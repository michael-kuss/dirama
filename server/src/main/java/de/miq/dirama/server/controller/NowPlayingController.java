package de.miq.dirama.server.controller;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.miq.dirama.server.model.Title;
import de.miq.dirama.server.repository.TitleRepository;
import de.miq.dirama.server.services.TriggersActionService;

@RestController
@RequestMapping(value = "/nowplaying", produces = "application/json")
public class NowPlayingController {
    private static final Log LOG = LogFactory
            .getLog(NowPlayingController.class);

    private static final DateFormat DATEFORMAT = new SimpleDateFormat(
            "yyyyMMddHHmmss");

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private TriggersActionService triggerService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public void addNowPlaying(
            @RequestBody List<Title> titles,
            @RequestParam(value = "ignoreNow", defaultValue = "false") boolean ignoreNow,
            @RequestParam(value = "trigger", defaultValue = "true") boolean trigger) {
        for (Title title : titles) {
            addNowPlaying(title.getStation(), title.getArtist(),
                    title.getTitle(), title.getDabImage(), title.getWebImage(),
                    DATEFORMAT.format(title.getTime()), title.getAdditional1(),
                    title.getAdditional2(), title.getAdditional3(),
                    title.getAdditional4(), title.getAdditional5(),
                    title.getProperties(), ignoreNow, trigger, false);
        }
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = { "/{station}" })
    public void addNowPlaying(
            @PathVariable("station") String station,
            @RequestParam("artist") String artist,
            @RequestParam("title") String title,
            @RequestParam("dabImage") String dabImage,
            @RequestParam("webImage") String webImage,
            @RequestParam("time") String time,
            @RequestParam(value = "additional1", required = false) String additional1,
            @RequestParam(value = "additional2", required = false) String additional2,
            @RequestParam(value = "additional3", required = false) String additional3,
            @RequestParam(value = "additional4", required = false) String additional4,
            @RequestParam(value = "additional5", required = false) String additional5,
            @RequestParam Map<String, String> requestParams,
            @RequestParam(value = "ignoreNow", defaultValue = "false") boolean ignoreNow,
            @RequestParam(value = "trigger", defaultValue = "true") boolean trigger,
            @RequestParam(value = "doDecode", defaultValue = "false") boolean doDecode) {

        Title tt;
        Date now = new Date();
        Date date = null;

        // decode if needed
        if (doDecode) {
            artist = decode(artist);
            title = decode(title);
            dabImage = decode(dabImage);
            webImage = decode(webImage);
            time = decode(time);
            additional1 = decode(additional1);
            additional2 = decode(additional2);
            additional3 = decode(additional3);
            additional4 = decode(additional4);
            additional5 = decode(additional5);
        }

        try {
            date = DATEFORMAT.parse(time);
        } catch (Throwable e) {
            LOG.error(e.getMessage() + "; time=" + time, e);
            date = new Date();
        }
        if (!ignoreNow) {
            Date before = DateUtils.addMinutes(now, 10);
            Date after = DateUtils.addMinutes(now, -10);

            if (!(date.after(after) && date.before(before))) {
                throw new IllegalStateException("Date not now");
            }
        }

        tt = new Title(station, artist, title, dabImage, webImage, date,
                additional1, additional2, additional3, additional4, additional5);

        if (requestParams != null) {
            for (String key : requestParams.keySet()) {
                if (key.startsWith("PROP.")) {
                    LOG.info("ADD PROP " + key + "=" + requestParams.get(key));
                    tt.addProperty(key, requestParams.get(key));
                }
            }
        }

        titleRepository.index(tt);
        LOG.info("Added " + tt);

        if (trigger) {
            triggerService.executeTriggers(tt);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = { "/{station}" })
    public List<Title> requestNowPlaying(
            @PathVariable("station") String station,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size,
            @RequestParam(value = "full", defaultValue = "false") boolean full) {
        if (titleRepository.count() <= 0) {
            return null;
        }
        Pageable pageable = new PageRequest(page, size, new Sort(
                Direction.DESC, "time"));
        Page<Title> titles = titleRepository.findByStation(station, pageable);
        if (titles == null) {
            return null;
        }

        return filterTitle(full, titles);
    }

    public static List<Title> filterTitle(boolean full, Page<Title> titles) {
        if (full) {
            return titles.getContent();
        } else {
            List<Title> ret = new ArrayList<Title>();
            for (Title title : titles.getContent()) {
                Title t = new Title(null, title);
                ret.add(t);
            }
            return ret;
        }
    }

    private String decode(String str) {
        try {
            return URLDecoder.decode(str, "ISO-8859-1");
        } catch (Throwable t) {
            return str;
        }
    }
}
