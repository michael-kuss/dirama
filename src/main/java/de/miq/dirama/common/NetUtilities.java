/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.common;

import de.miq.dirama.dto.trigger.FtpEntry;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.*;
import org.apache.commons.net.util.TrustManagerUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class NetUtilities {
  private static final int BUFFER = 1024 * 1024;
  private static final int CONNECTION_TIMEOUT = 20000;
  private static final String DYNAMICLABEL =
      "https://{contentServer}:24/?login={user}&password={passwd}&type=import&really=1&content={content}";
  private static final String SLIDESHOW =
      "https://{contentServer}:24/?login={user}&password={passwd}&type=import";

  private NetUtilities() {}

  public static void getData(RestTemplate template, String url, List<String> params) {
    try {
      template.getForObject(url, String.class, params.toArray());
    } catch (RestClientException e) {
      log.error("error using http get for {} : {}", url, params, e);
    }
  }

  public static void updateDynamicLabel(
      RestTemplate template,
      String contentServer,
      String user,
      String password,
      String dynamicLabel) {
    try {

      template.getForObject(
          DYNAMICLABEL,
          String.class,
          contentServer,
          user,
          password,
          StringUtils.left(dynamicLabel, 128));
    } catch (RestClientException e) {
      log.error("error updating dynamic label", e);
    }
  }

  public static void updateSlideshow(
      RestTemplate template, String contentServer, String user, String password) {
    try {
      template.getForObject(SLIDESHOW, String.class, contentServer, user, password);
    } catch (RestClientException e) {
      log.error("error updating slideshow", e);
    }
  }

  public static void uploadToFtp(
      String ftpServer,
      String user,
      String password,
      String fileName,
      InputStream fileStream,
      boolean enableTLS) {
    uploadToFtp(ftpServer, user, password, fileName, fileStream, null, enableTLS);
  }

  public static void uploadToFtp(
      String ftpServer,
      String user,
      String password,
      String fileName,
      InputStream fileStream,
      String deleteFileName,
      boolean enableTLS) {
    int retryCount = 0;
    while (true) {
      try {
        FTPClient ftp = null;
        try {
          ftp = login(ftpServer, user, password, enableTLS);

          if (deleteFileName != null) {
            ftp.deleteFile(deleteFileName);
          }

          String fileTest = fileName.toLowerCase();
          if (fileTest.endsWith("txt")
              || fileTest.endsWith("htm")
              || fileTest.endsWith("html")
              || fileTest.endsWith("json")) {
            ftp.setFileType(FTP.ASCII_FILE_TYPE);
          } else {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
          }

          try {
            long start = System.currentTimeMillis();
            log.info("upload to ftp {}@{}/{}", user, ftpServer, fileName);
            ftp.storeFile(fileName, new BufferedInputStream(fileStream, BUFFER));
            log.info(
                "upload finished to ftp {}@{}/{} in {}ms",
                user,
                ftpServer,
                fileName,
                System.currentTimeMillis() - start);
          } finally {
            IOUtils.closeQuietly(fileStream);
          }
        } finally {
          logout(ftp);
        }
        break;
      } catch (Throwable e) {
        log.error("Error during file upload, resetting connection for <{}>", fileName, e);
        retryCount++;

        if (retryCount > 3) {
          log.error("Dismissing upload as failed <{}>", fileName);
          break;
        }
      }
    }
  }

  public static void uploadToFtp(
      String ftpServer, String user, String password, List<File> files, boolean enableTLS) {
    try {
      FTPClient ftp = null;
      try {
        ftp = login(ftpServer, user, password, enableTLS);

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

            InputStream fileStream = new BufferedInputStream(new FileInputStream(file), BUFFER);

            try {
              long start = System.currentTimeMillis();
              log.info("upload to ftp {}@{}/{}", user, ftpServer, file.getName());
              ftp.storeFile(file.getName(), fileStream);
              log.info(
                  "upload finished to ftp {}@{}/{} in {}ms",
                  user,
                  ftpServer,
                  file.getName(),
                  System.currentTimeMillis() - start);
            } finally {
              IOUtils.closeQuietly(fileStream);
            }
          } catch (Throwable e) {
            log.error("Error during file upload, resetting connection for <{}>", file.getName(), e);
            logout(ftp);
            ftp = login(ftpServer, user, password, enableTLS);
            retryCount++;
            if (!file.equals(last)) {
              pos--;
            }

            if (retryCount > 3) {
              log.error("Dismissing upload as failed <{}>", file.getName());
              break;
            }
          }
          last = file;
        }
      } finally {
        logout(ftp);
      }
    } catch (Throwable e) {
      log.error(e.getMessage(), e);
    }
  }

  public static List<FtpEntry> listFtp(
      String ftpServer, String user, String password, String dir, boolean enableTLS) {
    List<FtpEntry> ret = new ArrayList<>();
    try {
      FTPClient ftp = null;
      try {
        ftp = login(ftpServer, user, password, enableTLS);

        FTPFile[] files = ftp.listFiles(dir);
        for (FTPFile file : files) {
          ret.add(
              new FtpEntry(
                  file.getName(), file.getSize(), file.getTimestamp().getTime(), file.getType()));
        }

      } finally {
        logout(ftp);
      }
    } catch (Throwable e) {
      log.error(e.getMessage(), e);
    }
    return ret;
  }

  public static byte[] getFtpFile(
      String ftpServer, String user, String password, String file, boolean enableTLS) {
    try {
      FTPClient ftp = null;
      try {
        ftp = login(ftpServer, user, password, enableTLS);

        ftp.setFileType(FTP.BINARY_FILE_TYPE);

        InputStream is = ftp.retrieveFileStream(file);
        byte[] tmp = IOUtils.toByteArray(is);
        IOUtils.closeQuietly(is);

        return tmp;
      } finally {
        logout(ftp);
      }
    } catch (Throwable e) {
      log.error(e.getMessage(), e);
    }
    return null;
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

  private static FTPClient login(String ftpServer, String user, String password, boolean enableTLS)
      throws IOException {
    if (ftpServer == null || ftpServer.trim().isEmpty()) {
      return null;
    }

    FTPClient ftp;

    if (enableTLS) {
      FTPSClient ftps = new FTPSClient();
      // ftps.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out),
      // true));
      ftps.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
      ftps.setEnabledSessionCreation(true);
      ftp = ftps;
    } else {
      ftp = new FTPClient();
    }

    ftp.setRemoteVerificationEnabled(false);
    ftp.setControlKeepAliveTimeout(Duration.ofSeconds(60));
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
      log.error("FTP server refused connection.");
    }

    if (!ftp.login(user, password)) {
      log.error("FTP login failed!");
      ftp.logout();
      return null;
    }

    ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
    ftp.enterLocalPassiveMode();
    if (enableTLS) {
      ((FTPSClient) ftp).execPROT("P");
    }

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

  public static void main(String[] args) throws Exception {
    List<File> files = new ArrayList<>();
    files.add(new File("C:/DPM_update.txt"));
    // files.add(new File("C:\\Users\\mail\\Pictures\\fho-ff-wallpaper-1-1920x1080.jpg"));

    // NetUtilities.uploadToFtp("localhost:9090", "user", "password", files);
    /**
     * NetUtilities.uploadToFtp("www439.your-server.de", "purenm_3", "yGzDwS93uDLv2vk3", files,
     * true); NetUtilities.uploadToFtp("www439.your-server.de", "purenm_2", "Oderturm2018#", files,
     * true); for (FtpEntry f: NetUtilities.listFtp("87.138.102.27", "pure_sl", "KqNd7XRV", "/",
     * false)) { log.info(f.getName()); }
     */
    Files.copy(
        NetUtilities.getInputStream(
            "https://pure-fm.de/wp-content/uploads/sls-pure-berlinreport.jpg"),
        new File("out.jpg").toPath());

    Thread.sleep(1000);
  }
}
