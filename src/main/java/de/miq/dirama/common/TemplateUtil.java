/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.common;

import de.miq.dirama.entity.TitleEntity;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
public class TemplateUtil {
  private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");

  private TemplateUtil() {}

  public static synchronized void processTemplates(
      File templateDir,
      String ftpServer,
      String user,
      String password,
      boolean tls,
      List<TitleEntity> titles) {
    File uploadDir = new File(templateDir, "upload");
    if (!uploadDir.exists()) {
      if (uploadDir.mkdirs()) {
        log.warn("Could not create upload directory {}", uploadDir.getAbsolutePath());
      }
    }

    List<File> uploadFiles = new ArrayList<>();

    for (File template : findFiles(templateDir)) {
      try {
        String str = readFile(template.getAbsolutePath());

        int pos = 0;
        for (TitleEntity title : titles) {
          pos++;

          str = replace("COMMENT", str, pos, title.getWebImage(), template);
          str = replace("TITLE", str, pos, title.getTitle(), template);
          str = replace("ARTIST", str, pos, title.getArtist(), template);
          str = replace("TIME", str, pos, TIME.format(title.getTitleDate()), template);
          str = replace("WEBIMAGE", str, pos, title.getWebImage(), template);
          str = replace("DABIMAGE", str, pos, title.getDabImage(), template);
          str = replace("ADDITIONAL1_", str, pos, title.getAdditional1(), template);
          str = replace("ADDITIONAL2_", str, pos, title.getAdditional2(), template);
          str = replace("ADDITIONAL3_", str, pos, title.getAdditional3(), template);
          str = replace("ADDITIONAL4_", str, pos, title.getAdditional4(), template);
          str = replace("ADDITIONAL5_", str, pos, title.getAdditional5(), template);
        }

        File uploadFile = new File(uploadDir, template.getName());

        try (Writer out = Files.newBufferedWriter(uploadFile.toPath())) {
          out.write(str);
        }

        uploadFiles.add(uploadFile);

      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }

    NetUtilities.uploadToFtp(ftpServer, user, password, uploadFiles, tls);
    try {
      FileUtils.deleteDirectory(uploadDir);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  private static String replace(String token, String str, int pos, String entry, File template) {
    if (entry == null) {
      return str;
    }

    try {
      return str.replaceAll("@" + token + pos + "@", entry.replace("$", "╔")).replace("╔", "$");
    } catch (Exception e) {
      log.error("ERROR with <" + token + pos + "> for <" + entry + "> in " + template.getName());
    }
    return str;
  }

  private static List<File> findFiles(File templateDir) {
    File[] templateFiles =
        templateDir.listFiles(
            (dir, name) ->
                (name.contains("playlist") || name.contains("webplayer")) && name.contains(".htm"));

    if (templateFiles == null || templateFiles.length == 0) {
      return Collections.emptyList();
    }

    return Arrays.asList(templateFiles);
  }

  private static String readFile(String path) throws IOException {
    return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
  }
}
