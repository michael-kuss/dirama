package de.miq.dirama.server.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
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
    private static final int BUFFER = 1024 * 1024;
    private static final int CONNECTION_TIMEOUT = 10000;
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
        int retryCount = 0;
        while (true) {
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

                    try {
                        long start = System.currentTimeMillis();
                        LOG.info("upload to ftp " + user + "@" + ftpServer
                                + "/" + fileName);
                        ftp.storeFile(fileName, new BufferedInputStream(
                                fileStream, BUFFER));
                        LOG.info("upload finished to ftp " + user + "@"
                                + ftpServer + "/" + fileName + " in "
                                + (System.currentTimeMillis() - start) + "ms");
                    } finally {
                        IOUtils.closeQuietly(fileStream);
                    }
                } finally {
                    logout(ftp);
                }
                break;
            } catch (Throwable e) {
                LOG.error(
                        "Error during file upload, resetting connection for <"
                                + fileName + ">", e);
                retryCount++;

                if (retryCount > 3) {
                    LOG.error("Dismissing upload as failed <" + fileName + ">");
                    break;
                }
            }
        }
    }

    public static void uploadToFtp(String ftpServer, String user,
            String password, List<File> files) {
        try {
            FTPClient ftp = null;
            try {
                ftp = login(ftpServer, user, password);

                File last = null;
                int retryCount = 0;
                for (int pos = 0; pos < files.size(); pos++) {
                    File file = files.get(pos);
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
                                new FileInputStream(file), BUFFER);

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
                    } catch (Throwable e) {
                        LOG.error(
                                "Error during file upload, resetting connection for <"
                                        + file.getName() + ">", e);
                        logout(ftp);
                        ftp = login(ftpServer, user, password);
                        retryCount++;
                        if (!file.equals(last)) {
                            pos--;
                        }

                        if (retryCount > 3) {
                            LOG.error("Dismissing upload as failed <"
                                    + file.getName() + ">");
                            break;
                        }
                    }
                    last = file;
                }
            } finally {
                logout(ftp);
            }
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private static void logout(FTPClient ftp) {
        try {
            ftp.logout();
        } catch (Throwable e) {
            // do nothing
        }
        try {
            ftp.disconnect();
        } catch (Throwable e) {
            // do nothing
        }
    }

    private static FTPClient login(String ftpServer, String user,
            String password) throws IOException {
        if (ftpServer == null || ftpServer.trim().equals("")) {
            return null;
        }

        FTPClient ftp = new FTPClient();
        ftp.setRemoteVerificationEnabled(false);
        ftp.setControlKeepAliveTimeout(30);
        ftp.setConnectTimeout(CONNECTION_TIMEOUT);
        ftp.setAutodetectUTF8(true);

        if (ftpServer.contains(":")) {
            int index = ftpServer.indexOf(":");
            String host = ftpServer.substring(0, index);
            String port = ftpServer.substring(index + 1);
            ftp.connect(host, Integer.parseInt(port));
        } else {
            ftp.connect(ftpServer);
        }

        ftp.setSoTimeout(CONNECTION_TIMEOUT);
        ftp.setTcpNoDelay(true);
        ftp.setSoLinger(true, CONNECTION_TIMEOUT);

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

        ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
        ftp.enterLocalPassiveMode();

        return ftp;
    }

    public static InputStream getInputStream(String url) throws IOException {
        URLConnection con = new URL(url).openConnection();
        con.setConnectTimeout(CONNECTION_TIMEOUT);
        con.setReadTimeout(CONNECTION_TIMEOUT);
        con.setUseCaches(true);
        try {
            return con.getInputStream();
        } catch (IOException ex) {
            // Close the HTTP connection (if applicable).
            if (con instanceof HttpURLConnection) {
                ((HttpURLConnection) con).disconnect();
            }
            throw ex;
        }
    }

    public static void main(String[] args) {
        List<File> files = new ArrayList<File>();
        files.add(new File("C:/zl.txt"));

        NetUtilities.uploadToFtp("localhost:9090", "user", "password", files);
        NetUtilities.uploadToFtp("localhost", "user", "password", files);
    }
}
