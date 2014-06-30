package de.miq.dirama.server.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import de.miq.dirama.server.config.TriggerActionConfig;
import de.miq.dirama.server.config.TriggerActionConfig.TriggerProperty;
import de.miq.dirama.server.repository.TitleRepository;
import de.miq.dirama.server.util.NetUtilities;
import de.miq.dirama.server.util.TemplateUtil;

@Service
public class TriggerActionService {
    private static final Log LOG = LogFactory
            .getLog(TriggerActionService.class);

    @Autowired
    private TriggerActionConfig triggerConf;

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private RestTemplate restTemplate;

    private Map<String, String> mapConfig;

    /**
     * Needs:
     * - $id.ftpServer
     * - $id.user
     * - $id.password
     * 
     * @param id
     * @param fileName
     * @param fileUrl
     * @param alternateUrl
     */
    @Async
    public boolean actionSendFile(String id, String fileName, String fileUrl,
            String alternateUrl) {
        try {
            String ftpServer = getValue(id + ".ftpServer");
            String user = getValue(id + ".user");
            String password = getValue(id + ".password");
            InputStream fileStream = null;

            try {
                URL url = new URL(fileUrl);
                fileStream = url.openStream();
            } catch (IOException e) {
                LOG.error("URL not found <" + fileUrl + ">");
                URL url = new URL(alternateUrl);
                fileStream = url.openStream();
            }

            NetUtilities.uploadToFtp(ftpServer, user, password, fileName,
                    fileStream);
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * Needs:
     * - $id.station
     * - $id.templateDir
     * - $id.uploadDir
     * - template.$station.ftpServer
     * - template.$station.user
     * - template.$station.password
     * 
     * @param id
     */
    @Async
    public boolean actionPlaylist(String id) {
        try {
            String station = getValue(id + ".station");
            File templateDir = new File(getValue(id + ".templateDir"));
            File uploadDir = new File(getValue(id + ".uploadDir"));
            String ftpServer = getValue("template." + station + ".ftpServer");
            String user = getValue("template." + station + ".user");
            String password = getValue("template." + station + ".password");

            TemplateUtil.processTemplates(station, templateDir, uploadDir,
                    ftpServer, user, password, titleRepository);
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean actionDynamicLabel(String id, String dynamicLabel) {
        try {
            String contentServer = getValue(id + ".contentServer");
            String user = getValue(id + ".user");
            String password = getValue(id + ".password");

            NetUtilities.updateDynamicLabel(restTemplate, contentServer, user,
                    password, dynamicLabel);
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    private String getValue(String key) {
        synchronized (triggerConf) {
            if (mapConfig == null) {
                mapConfig = new HashMap<String, String>();
                for (TriggerProperty prop : triggerConf.getSettings()) {
                    mapConfig.put(prop.getName(), prop.getValue());
                }
            }
        }
        String value = mapConfig.get(key);
        if (value == null) {
            LOG.error("trigger key <" + key + "> not found!");
        }
        return value;
    }
}
