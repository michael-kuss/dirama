package de.miq.dirama.server.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import de.miq.dirama.server.model.Config;

public interface ConfigRepository extends
        ElasticsearchRepository<Config, String> {
}
