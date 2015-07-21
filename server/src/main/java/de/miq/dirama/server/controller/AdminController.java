package de.miq.dirama.server.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.miq.dirama.server.model.Title;
import de.miq.dirama.server.repository.TitleRepository;

@RestController
@RequestMapping(value = "/admin", produces = "application/json")
public class AdminController {
    private static final Log LOG = LogFactory.getLog(AdminController.class);

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteTitle(@PathVariable("id") String id) {
        titleRepository.delete(id);
        LOG.info("Deleted Title " + id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/replaceArtist/{token1}/{token2}")
    public void replaceArtist(@PathVariable("token1") String token1,
            @PathVariable("token2") String token2) {
        Pageable pageable = new PageRequest(0, 10, new Sort(Direction.DESC,
                "time"));
        while (pageable != null) {

            Page<Title> titles = titleRepository.findByArtist(token1, pageable);
            if (titles == null) {
                return;
            }

            for (Title title : titles.getContent()) {
                title.setArtist(title.getArtist().replaceAll(token1, token2));
                titleRepository.save(title);
            }
            pageable = titles.nextPageable();
        }
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/replaceTitle/{token1}/{token2}")
    public void replaceTitle(@PathVariable("token1") String token1,
            @PathVariable("token2") String token2) {
        Pageable pageable = new PageRequest(0, 10, new Sort(Direction.DESC,
                "time"));
        while (pageable != null) {

            Page<Title> titles = titleRepository.findByTitle(token1, pageable);
            if (titles == null) {
                return;
            }

            for (Title title : titles.getContent()) {
                title.setTitle(title.getTitle().replaceAll(token1, token2));
                titleRepository.save(title);
            }
            pageable = titles.nextPageable();
        }
    }
}
