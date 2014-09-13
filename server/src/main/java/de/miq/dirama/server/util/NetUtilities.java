package de.miq.dirama.server.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class NetUtilities {
    private static final Log LOG = LogFactory.getLog(NetUtilities.class);
    private static final String DYNAMICLABEL = "https://{contentServer}:24/?login={user}&password={passwd}&type=import&really=1&content={content}";
    private static final String SLIDESHOW = "https://{contentServer}:24/?login={user}&password={passwd}&type=import";

    private NetUtilities() {

    }

    public static void getData(RestTemplate template, String url,
            List<String> params) {
        try {
            template.getForObject(url, String.class, params.toArray());
        } catch (RestClientException e) {
            LOG.error("error using http get for " + url + " : " + params, e);
        }
    }

    public static void updateDynamicLabel(RestTemplate template,
            String contentServer, String user, String password,
            String dynamicLabel) {
        try {

            template.getForObject(DYNAMICLABEL, String.class, contentServer,
                    user, password, StringUtils.left(dynamicLabel, 128));
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
        uploadToFtp(ftpServer, user, password, fileName, fileStream, null);
    }

    public static void uploadToFtp(String ftpServer, String user,
            String password, String fileName, InputStream fileStream,
            String deleteFileName) {
        try {
            FTPClient ftp = null;
            try {
                ftp = login(ftpServer, user, password);

                if (deleteFileName != null) {
                    ftp.deleteFile(deleteFileName);
                }

                String fileTest = fileName.toLowerCase();
                if (fileTest.endsWith("txt") || fileTest.endsWith("htm")
                        || fileTest.endsWith("html")
                        || fileTest.endsWith("json")) {
                    ftp.setFileType(FTP.ASCII_FILE_TYPE);
                } else {
                    ftp.setFileType(FTP.BINARY_FILE_TYPE);
                }

                LOG.info("upload to ftp " + user + "@" + ftpServer + "/"
                        + fileName);
                ftp.storeFile(fileName, fileStream);
            } finally {
                if (ftp != null && ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (IOException e) {
                        // do nothing
                    }
                }
                IOUtils.closeQuietly(fileStream);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static void uploadToFtp(String ftpServer, String user,
            String password, List<File> files) {
        try {
            FTPClient ftp = null;
            try {
                ftp = login(ftpServer, user, password);

                for (File file : files) {
                    try {
                        String fileTest = file.getName().toLowerCase();
                        if (fileTest.endsWith("txt")
                                || fileTest.endsWith("htm")
                                || fileTest.endsWith("html")
                                || fileTest.endsWith("json")) {
                            ftp.setFileType(FTP.ASCII_FILE_TYPE);
                        } else {
                            ftp.setFileType(FTP.BINARY_FILE_TYPE);
                        }
                        InputStream fileStream = new BufferedInputStream(
                                new FileInputStream(file));

                        try {
                            long start = System.currentTimeMillis();
                            LOG.info("upload to ftp " + user + "@" + ftpServer
                                    + "/" + file.getName());
                            ftp.storeFile(file.getName(), fileStream);
                            LOG.info("upload finished to ftp " + user + "@"
                                    + ftpServer + "/" + file.getName() + " in "
                                    + (System.currentTimeMillis() - start)
                                    + "ms");
                        } finally {
                            IOUtils.closeQuietly(fileStream);
                        }
                    } catch (Exception e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
            } finally {
                if (ftp != null && ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (IOException e) {
                        // do nothing
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private static FTPClient login(String ftpServer, String user,
            String password) throws IOException {
        FTPClient ftp = new FTPClient();
        ftp.setConnectTimeout(30000);
        ftp.setDefaultTimeout(30000);
        ftp.setAutodetectUTF8(true);
        //ftp.setTcpNoDelay(true);

        ftp.connect(ftpServer);

        int reply = ftp.getReplyCode();

        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            LOG.error("FTP server refused connection.");
        }

        if (!ftp.login(user, password)) {
            LOG.error("FTP login failed!");
            ftp.logout();
            return null;
        }

        ftp.setFileTransferMode(FTP.COMPRESSED_TRANSFER_MODE);
        ftp.enterLocalPassiveMode();

        return ftp;
    }
}
