package de.miq.dirama.server.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import de.miq.dirama.server.controller.NowPlayingController;
import de.miq.dirama.server.model.Title;
import de.miq.dirama.server.repository.TitleRepository;

public class TemplateUtil {
    private static final DateFormat TIME = new SimpleDateFormat("HH:mm");

    private static final Log LOG = LogFactory
            .getLog(NowPlayingController.class);

    private TemplateUtil() {

    }

    public static synchronized void processTemplates(String station,
            File templateDir, File uploadDir, String ftpServer, String user,
            String password, TitleRepository titleRepository) {
        Pageable pageable = new PageRequest(0, 50, new Sort(Direction.DESC,
                "time"));
        List<Title> titles = titleRepository.findByStation(station, pageable)
                .getContent();

        List<File> uploadFiles = new ArrayList<File>();

        for (File template : findFiles(templateDir)) {
            try {
                String str = readFile(template.getAbsolutePath(),
                        StandardCharsets.UTF_8);

                int pos = 0;
                for (Title title : titles) {
                    pos++;

                    str = replace("COMMENT", str, pos, title.getWebImage(),
                            template);
                    str = replace("TITLE", str, pos, title.getTitle(), template);
                    str = replace("ARTIST", str, pos, title.getArtist(),
                            template);
                    str = replace("TIME", str, pos,
                            TIME.format(title.getTime()), template);
                    str = replace("WEBIMAGE", str, pos, title.getWebImage(),
                            template);
                    str = replace("DABIMAGE", str, pos, title.getDabImage(),
                            template);
                    str = replace("ADDITIONAL1_", str, pos,
                            title.getAdditional1(), template);
                    str = replace("ADDITIONAL2_", str, pos,
                            title.getAdditional2(), template);
                    str = replace("ADDITIONAL3_", str, pos,
                            title.getAdditional3(), template);
                    str = replace("ADDITIONAL4_", str, pos,
                            title.getAdditional4(), template);
                    str = replace("ADDITIONAL5_", str, pos,
                            title.getAdditional5(), template);
                    if (title.getProperties() != null) {
                        for (String key : title.getProperties().keySet()) {
                            str = replace(key, str, pos,
                                    title.getProperty(key), template);
                        }
                    }
                }

                File uploadFile = new File(uploadDir, template.getName());
                Writer out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(uploadFile), "UTF-8"));
                try {
                    out.write(str);
                } finally {
                    try {
                        out.flush();
                    } catch (Throwable t) {
                    }
                    IOUtils.closeQuietly(out);
                }

                uploadFiles.add(uploadFile);

            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }

        NetUtilities.uploadToFtp(ftpServer, user, password, uploadFiles);
    }

    private static String replace(String token, String str, int pos,
            String entry, File template) {
        if (entry == null) {
            return str;
        }

        try {
            return str.replaceAll("@" + token + pos + "@",
                    entry.replace("$", "╔")).replace("╔", "$");
        } catch (Exception e) {
            System.err.println("ERROR with <" + token + pos + "> for <" + entry
                    + "> in " + template.getName());
        }
        return str;
    }

    private static List<File> findFiles(File templateDir) {
        File[] templateFiles = templateDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.contains("playlist") || name.contains("webplayer"))
                        && name.contains(".htm");
            }
        });

        return Arrays.asList(templateFiles);
    }

    private static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return encoding.decode(ByteBuffer.wrap(encoded)).toString();
    }

}
