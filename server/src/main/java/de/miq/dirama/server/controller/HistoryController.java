package de.miq.dirama.server.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.miq.dirama.server.model.Title;
import de.miq.dirama.server.repository.TitleRepository;

@RestController
@RequestMapping(value = "/history", produces = "application/json")
public class HistoryController {
    private static final Log LOG = LogFactory.getLog(HistoryController.class);

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @RequestMapping(method = RequestMethod.GET, value = { "/artist/{artist}" })
    public List<Title> requestArtist(@PathVariable("artist") String artist,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size,
            @RequestParam(value = "full", defaultValue = "false") boolean full) {
        if (titleRepository.count() <= 0) {
            return null;
        }
        Pageable pageable = new PageRequest(page, size, new Sort(
                Direction.DESC, "time"));
        Page<Title> titles = titleRepository.findByArtist(artist, pageable);
        if (titles == null) {
            return null;
        }

        return NowPlayingController.filterTitle(full, titles);
    }

    @RequestMapping(method = RequestMethod.GET, value = { "/title/{title}" })
    public List<Title> requestTitle(@PathVariable("title") String title,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size,
            @RequestParam(value = "full", defaultValue = "false") boolean full) {
        if (titleRepository.count() <= 0) {
            return null;
        }
        Pageable pageable = new PageRequest(page, size, new Sort(
                Direction.DESC, "time"));
        Page<Title> titles = titleRepository.findByTitle(title, pageable);
        if (titles == null) {
            return null;
        }

        return NowPlayingController.filterTitle(full, titles);
    }

    @RequestMapping(method = RequestMethod.GET, value = { "/stations" })
    public List<String> requestStations() {
        if (titleRepository.count() <= 0) {
            return null;
        }
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withSearchType(SearchType.COUNT)
                .withIndices("dirama-titles")
                .withTypes("title")
                .addAggregation(
                        AggregationBuilders.terms("stations").field("station"))
                .build();

        Aggregations aggregations = esTemplate.query(searchQuery,
                new ResultsExtractor<Aggregations>() {
                    @Override
                    public Aggregations extract(SearchResponse response) {
                        return response.getAggregations();
                    }
                });

        List<String> ret = new ArrayList<String>();

        for (Aggregation aggr : aggregations.asList()) {
            try {
                StringTerms term = (StringTerms) aggr;
                for (Terms.Bucket b : term.getBuckets()) {
                    ret.add(b.getKey());
                }
            } catch (ClassCastException e) {
            }
        }

        return ret;
    }

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
