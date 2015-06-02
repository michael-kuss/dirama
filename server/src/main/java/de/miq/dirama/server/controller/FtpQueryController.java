package de.miq.dirama.server.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.miq.dirama.server.model.FtpEntry;
import de.miq.dirama.server.services.ConfigService;
import de.miq.dirama.server.util.NetUtilities;

@RestController
@RequestMapping(value = "/ftp", produces = "application/json")
public class FtpQueryController {
    private static final Log LOG = LogFactory.getLog(FtpQueryController.class);

    @Autowired
    private ConfigService config;

    @RequestMapping(method = RequestMethod.GET, value = "/query/{ftpConfig}")
    public List<FtpEntry> ftpQuery(@PathVariable("ftpConfig") String id,
            @PathParam("dir") String dir) {
        LOG.info("Execute ftp query <" + id + "> <" + dir + ">");
        try {
            String ftpServer = config.getConfig(id + ".ftpServer");
            String user = config.getConfig(id + ".user");
            String password = config.getConfig(id + ".password");

            if (ftpServer == null || user == null || password == null) {
                LOG.error("Configuration error for actionUpdateLabelXML('" + id
                        + "')");
                return null;
            }

            return NetUtilities.listFtp(ftpServer, user, password, dir);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get/{ftpConfig}")
    public ResponseEntity<byte[]> ftpGet(@PathVariable("ftpConfig") String id,
            @PathParam("file") String file) {
        LOG.info("Execute ftp get <" + id + "> <" + file + ">");
        try {
            String ftpServer = config.getConfig(id + ".ftpServer");
            String user = config.getConfig(id + ".user");
            String password = config.getConfig(id + ".password");

            if (ftpServer == null || user == null || password == null) {
                LOG.error("Configuration error for actionUpdateLabelXML('" + id
                        + "')");
                return null;
            }
            HttpHeaders responseHeaders = new HttpHeaders();
            if (file.toLowerCase().endsWith(".jpg"))
                responseHeaders.setContentType(MediaType.IMAGE_JPEG);
            else if (file.toLowerCase().endsWith(".png"))
                responseHeaders.setContentType(MediaType.IMAGE_PNG);
            else if (file.toLowerCase().endsWith(".html")
                    || file.toLowerCase().endsWith(".htm"))
                responseHeaders.setContentType(MediaType.TEXT_HTML);
            else
                responseHeaders.setContentType(MediaType.TEXT_PLAIN);

            return new ResponseEntity<byte[]>(NetUtilities.getFtpFile(
                    ftpServer, user, password, file), responseHeaders,
                    HttpStatus.CREATED);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
}
