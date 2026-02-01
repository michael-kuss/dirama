/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.service.trigger;

import de.miq.dirama.common.NetUtilities;
import de.miq.dirama.common.TemplateUtil;
import de.miq.dirama.common.Triggers;
import de.miq.dirama.entity.StationEntity;
import de.miq.dirama.entity.TitleEntity;
import de.miq.dirama.repository.TitleRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Slf4j
public class SendFileToFtpTrigger extends AbstractTrigger {

  private final TitleRepository titleRepository;

  public SendFileToFtpTrigger(TitleRepository titleRepository) {
    this.titleRepository = titleRepository;
  }

  public static String FTP_SERVER_URL = "ftpServerUrl";
  public static String FTP_SERVER_USERNAME = "ftpServerUsername";
  public static String FTP_SERVER_PASSWORD = "ftpServerPassword";
  public static String FTP_SERVER_TLS = "ftpServerTls";
  public static String FTP_FILENAME = "ftpFileName";
  public static String SOURCE_URL = "sourceUrl";
  public static String DEFAULT_URL = "defaultUrl";

  private final List<String> MANDATORY_KEYS =
      Arrays.asList(
          FTP_SERVER_URL,
          FTP_SERVER_USERNAME,
          FTP_SERVER_PASSWORD,
          FTP_FILENAME,
          SOURCE_URL,
          DEFAULT_URL,
          DEFAULT_URL);
  private final List<String> OPTIONA_KEYS = Collections.singletonList(FTP_SERVER_TLS);

  @Override
  public void logExecute(StationEntity station) {
    try {
      Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "titleDate"));
      List<TitleEntity> titles =
          titleRepository.findByStationOrderByTitleDateDesc(station, pageable).getContent();

      if (titles.isEmpty()) {
        return;
      }

      String ftpServer = getValue(FTP_SERVER_URL);
      String user = getValue(FTP_SERVER_USERNAME);
      String password = getValue(FTP_SERVER_PASSWORD);
      boolean tls = Boolean.parseBoolean(getValue(FTP_SERVER_TLS));
      String fileName = getValue(FTP_FILENAME);
      String urlUsed = getValue(SOURCE_URL);
      String defaultUrl = getValue(DEFAULT_URL);

      fileName = TemplateUtil.replace(fileName, titles.get(0));
      urlUsed = TemplateUtil.replace(urlUsed, titles.get(0));
      defaultUrl = TemplateUtil.replace(defaultUrl, titles.get(0));

      InputStream fileStream;

      try {
        fileStream = NetUtilities.getInputStream(urlUsed);
      } catch (IOException e) {
        log.error("URL not found <{}>, using <{}>", urlUsed, defaultUrl);
        urlUsed = defaultUrl;
        fileStream = NetUtilities.getInputStream(defaultUrl);
      }

      // png or jpg. delete old file first
      String extGiven = FilenameUtils.getExtension(fileName);
      String extReal = FilenameUtils.getExtension(urlUsed);

      if (!extGiven.equals(extReal)) {
        NetUtilities.uploadToFtp(
            ftpServer,
            user,
            password,
            fileName.replace(extGiven, extReal),
            fileStream,
            fileName,
            tls);
      } else {
        NetUtilities.uploadToFtp(ftpServer, user, password, fileName, fileStream, tls);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
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
    return Triggers.SEND_FILE_TO_FTP;
  }
}
