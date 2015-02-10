package de.miq.dirama.server.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@EnableElasticsearchRepositories("de.miq.dirama.server.repository")
@EnableSpringDataWebSupport
@EnableScheduling
@Configuration
@ComponentScan(basePackages = { "de.miq.dirama.server.controller",
        "de.miq.dirama.server.config", "de.miq.dirama.server.services",
        "de.miq.dirama.server.repository" })
public class Application implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
    }

    public static void main(final String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args).registerShutdownHook();
    }
}
