package de.miq.dirama.server.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class NetUtilities {
    private static final Log LOG = LogFactory.getLog(NetUtilities.class);
    private static final String DYNAMICLABEL = "https://{contentServer}:24/?login={user}&password={passwd}&type=import&really=1&content={cotent}";
    private static final String SLIDESHOW = "https://{contentServer}:24/?login={user}&password={passwd}&type=import";

    private NetUtilities() {

    }

    public static void updateDynamicLabel(RestTemplate template,
            String contentServer, String user, String password,
            String dynamicLabel) {
        try {
            template.getForObject(DYNAMICLABEL, String.class, contentServer,
                    user, password, dynamicLabel);
        } catch (RestClientException e) {
            LOG.error("error updating dynamic label", e);
        }
    }

    public static void updateSlideshow(RestTemplate template,
            String contentServer, String user, String password) {
        try {
            template.getForObject(SLIDESHOW, String.class, contentServer, user,
                    password);
        } catch (RestClientException e) {
            LOG.error("error updating slideshow", e);
        }
    }

    public static void uploadToFtp(String ftpServer, String user,
            String password, String fileName, InputStream fileStream) {
        try {
            FTPClient ftp = new FTPClient();
            ftp.connect(ftpServer);

            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                LOG.error("FTP server refused connection.");
            }

            if (!ftp.login(user, password)) {
                LOG.error("FTP login failed!");
                ftp.logout();
                return;
            }

            ftp.enterLocalPassiveMode();

            ftp.storeFile(fileName, fileStream);
            IOUtils.closeQuietly(fileStream);

            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    // do nothing
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
