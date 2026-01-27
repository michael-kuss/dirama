/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.service.trigger;

import de.miq.dirama.common.TemplateUtil;
import de.miq.dirama.common.Triggers;
import de.miq.dirama.entity.StationEntity;
import de.miq.dirama.entity.TitleEntity;
import de.miq.dirama.repository.TitleRepository;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class RenderHtmlPlaylistTrigger extends AbstractTrigger {

  private final TitleRepository titleRepository;

  public RenderHtmlPlaylistTrigger(TitleRepository titleRepository) {
    this.titleRepository = titleRepository;
  }

  public static String TEMPLATE_DIR = "templateDir";
  public static String FTP_SERVER_URL = "ftpServerUrl";
  public static String FTP_SERVER_USERNAME = "ftpServerUsername";
  public static String FTP_SERVER_PASSWORD = "ftpServerPassword";
  public static String FTP_SERVER_TLS = "ftpServerTls";

  private final List<String> MANDATORY_KEYS =
      Arrays.asList(TEMPLATE_DIR, FTP_SERVER_URL, FTP_SERVER_USERNAME, FTP_SERVER_PASSWORD);
  private final List<String> OPTIONA_KEYS = Collections.singletonList(FTP_SERVER_TLS);

  @Override
  public void execute(StationEntity station) {
    Pageable pageable = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "titleDate"));
    List<TitleEntity> titles =
        titleRepository.findByStationOrderByTitleDateDesc(station, pageable).getContent();

    TemplateUtil.processTemplates(
        new File(getValue(TEMPLATE_DIR)),
        getValue(FTP_SERVER_URL),
        getValue(FTP_SERVER_USERNAME),
        getValue(FTP_SERVER_PASSWORD),
        Boolean.parseBoolean(getValue(FTP_SERVER_TLS)),
        titles);
  }

  @Override
  public List<String> getMandatoryKeys() {
    return MANDATORY_KEYS;
  }

  @Override
  public List<String> getOptionalKeys() {
    return OPTIONA_KEYS;
  }

  @Override
  public Triggers trigger() {
    return Triggers.RENDER_HTML_PLAYLIST;
  }
}
