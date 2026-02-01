/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.config.web;

import freemarker.template.TemplateModelException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FreemarkerConfiguration {
  @Value("${spring.application.name}")
  private String appName;

  @Autowired private freemarker.template.Configuration configuration;

  @PostConstruct
  public void configuration() throws TemplateModelException {
    // add globe variable
    this.configuration.setSharedVariable("app", appName);
  }
}
