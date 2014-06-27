package de.miq.dirama.server.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.miq.dirama.server.model.Title;
import de.miq.dirama.server.repository.TitleRepository;

@Controller
@RequestMapping("/nowplaying")
public class NowPlayingController {
    private static final DateFormat DATEFORMAT = new SimpleDateFormat(
            "yyyyMMddHHmmss");

    @Autowired
    private TitleRepository titleRepository;

    @RequestMapping(method = RequestMethod.POST, value = { "/{station}" })
    @ResponseBody
    public ResponseEntity<List<String>> addNowPlaying(
            @PathVariable("station") String station,
            @RequestParam("artist") String artist,
            @RequestParam("title") String title,
            @RequestParam("dabImage") String dabImage,
            @RequestParam("webImage") String webImage,
            @RequestParam("time") String time) {
        Title tt;
        try {
            System.out.println(time);
            Date date = DATEFORMAT.parse(time);
            tt = new Title(station, artist, title, dabImage, webImage, date);

            titleRepository.index(tt);
            System.out.println(station + " " + tt);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }


    @RequestMapping(method = RequestMethod.GET, value = { "/{station}" })
    @ResponseBody
    public List<Title> requestNowPlaying(
            @PathVariable("station") String station,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        Pageable pageable = new PageRequest(0, size, new Sort(Direction.DESC,
                "time"));
        Page<Title> titles = titleRepository.findByStation(station, pageable);
        if (titles == null) {
            return null;
        }

        return titles.getContent();
    }
}
