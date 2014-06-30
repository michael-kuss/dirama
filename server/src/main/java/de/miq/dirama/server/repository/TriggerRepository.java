package de.miq.dirama.server.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import de.miq.dirama.server.model.Trigger;

public interface TriggerRepository extends
        ElasticsearchRepository<Trigger, String> {
}
