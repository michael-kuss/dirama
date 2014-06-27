package de.miq.dirama.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import de.miq.dirama.server.model.Title;

public interface TitleRepository extends ElasticsearchRepository<Title, String> {
    Page<Title> findByStation(String station, Pageable pageable);

    Page<Title> findByArtist(String artist, Pageable pageable);

    Page<Title> findByTitle(String title, Pageable pageable);
}
