package de.miq.dirama.server.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import de.miq.dirama.server.model.Event;
import de.miq.dirama.server.model.Title;

public interface EventRepository extends ElasticsearchRepository<Event, String> {
    Page<Title> findByStartDate(Date startDate, Pageable pageable);

    Page<Title> findByStartDateAndEndDate(Date startDate, Date endDate,
            Pageable pageable);
}
