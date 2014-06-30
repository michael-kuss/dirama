package de.miq.dirama.server.config;

import org.elasticsearch.node.NodeBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableAutoConfiguration
@EnableAsync
@EnableElasticsearchRepositories("de.miq.dirama.server.repository")
@EnableSpringDataWebSupport
@EnableScheduling
@Configuration
@ComponentScan(basePackages = { "de.miq.dirama.server.controller",
        "de.miq.dirama.server.config", "de.miq.dirama.server.services",
        "de.miq.dirama.server.repository" })
public class Application implements CommandLineRunner {

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(NodeBuilder.nodeBuilder().local(true)
                .node().client());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void run(String... args) throws Exception {
    }

    public static void main(final String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args).registerShutdownHook();
    }

}
