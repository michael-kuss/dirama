package de.miq.dirama.server.config;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.miq.dirama.server.model.Config;
import de.miq.dirama.server.model.Event;
import de.miq.dirama.server.model.Title;
import de.miq.dirama.server.model.Trigger;

@EnableAutoConfiguration
@EnableElasticsearchRepositories("de.miq.dirama.server.repository")
@EnableSpringDataWebSupport
@EnableScheduling
@Configuration
@ComponentScan(basePackages = { "de.miq.dirama.server.controller",
        "de.miq.dirama.server.config", "de.miq.dirama.server.services",
        "de.miq.dirama.server.repository" })
public class Application implements CommandLineRunner {

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() {
        ImmutableSettings.Builder settings = ImmutableSettings
                .settingsBuilder().put("http.enabled", "false");

        ElasticsearchTemplate template = new ElasticsearchTemplate(NodeBuilder
                .nodeBuilder().settings(settings).local(true)
                .clusterName("dirama").node().client());

        if (!template.indexExists(Title.class)) {
            template.createIndex(Title.class);
            template.putMapping(Title.class);
        }
        if (!template.indexExists(Config.class)) {
            template.createIndex(Config.class);
            template.putMapping(Config.class);
        }
        if (!template.indexExists(Event.class)) {
            template.createIndex(Event.class);
            template.putMapping(Event.class);
        }
        if (!template.indexExists(Trigger.class)) {
            template.createIndex(Trigger.class);
            template.putMapping(Trigger.class);
        }
        return template;
    }

    @Override
    public void run(String... args) throws Exception {
    }

    public static void main(final String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args).registerShutdownHook();
    }
}
