package de.miq.dirama.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.miq.dirama.server.model.Title;
import de.miq.dirama.server.repository.TitleRepository;

@Controller
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private TitleRepository titleRepository;

    @RequestMapping(method = RequestMethod.GET, value = { "/artist/{artist}" })
    @ResponseBody
    public List<Title> requestArtist(@PathVariable("artist") String artist,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        Pageable pageable = new PageRequest(0, size, new Sort(Direction.DESC,
                "time"));
        Page<Title> titles = titleRepository.findByArtist(artist, pageable);
        if (titles == null) {
            return null;
        }

        return titles.getContent();
    }

    @RequestMapping(method = RequestMethod.GET, value = { "/title/{title}" })
    @ResponseBody
    public List<Title> requestTitle(@PathVariable("title") String title,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        Pageable pageable = new PageRequest(0, size, new Sort(Direction.DESC,
                "time"));
        Page<Title> titles = titleRepository.findByTitle(title, pageable);
        if (titles == null) {
            return null;
        }

        return titles.getContent();
    }
}
