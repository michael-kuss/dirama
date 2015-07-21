package de.miq.dirama.server.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.social.twitter.api.TweetData;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import de.miq.dirama.server.model.Title;
import de.miq.dirama.server.model.Trigger;
import de.miq.dirama.server.repository.TitleRepository;
import de.miq.dirama.server.util.NetUtilities;
import de.miq.dirama.server.util.TemplateUtil;

@Service
public class TriggerActionService {
    private static final Log LOG = LogFactory
            .getLog(TriggerActionService.class);

    private static List<Object> locks = Collections
            .synchronizedList(new ArrayList<Object>());

    @Autowired
    private ConfigService config;

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private RestTemplate restTemplate;

    private ExpressionParser expressionParser = new SpelExpressionParser();

    public List<Title> actionGetTitles(String station, int size) {
        Pageable pageable = new PageRequest(0, size, new Sort(Direction.DESC,
                "time"));
        List<Title> titles = titleRepository.findByStation(station, pageable)
                .getContent();

        return titles;
    }

    /**
     * Needs:
     * - $id.ftpServer
     * - $id.user
     * - $id.password
     * 
     * @param id
     * @param fileName
     * @param fileUrl
     */
    public boolean actionSendObject(String id, String fileName, Object object) {
        try {
            String ftpServer = config.getConfig(id + ".ftpServer");
            String user = config.getConfig(id + ".user");
            String password = config.getConfig(id + ".password");

            if (ftpServer == null || user == null || password == null) {
                LOG.error("Configuration error for actionSendObject('" + id
                        + "', ...)");
                return false;
            }

            LOG.info("actionSendObject('" + id + "', ...)");

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(object);

            NetUtilities.uploadToFtp(ftpServer, user, password, fileName,
                    IOUtils.toInputStream(json, "UTF-8"));

            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

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
    public boolean actionSendFile(String id, String fileName, String fileUrl,
            String alternateUrl) {
        try {
            String ftpServer = config.getConfig(id + ".ftpServer");
            String user = config.getConfig(id + ".user");
            String password = config.getConfig(id + ".password");

            if (ftpServer == null || user == null || password == null) {
                LOG.error("Configuration error for actionSendFile('" + id
                        + "', ...)");
                return false;
            }

            LOG.info("actionSendFile('" + id + "', ...)");

            InputStream fileStream = null;

            String urlUsed = fileUrl;

            try {
                fileStream = NetUtilities.getInputStream(fileUrl);
            } catch (IOException e) {
                LOG.error("URL not found <" + fileUrl + ">");
                urlUsed = alternateUrl;
                fileStream = NetUtilities.getInputStream(alternateUrl);
            }

            // png or jpg. delete old file first
            String extGiven = FilenameUtils.getExtension(fileName);
            String extReal = FilenameUtils.getExtension(urlUsed);

            if (!extGiven.equals(extReal)) {
                NetUtilities.uploadToFtp(ftpServer, user, password,
                        fileName.replace(extGiven, extReal), fileStream,
                        fileName);
            } else {
                NetUtilities.uploadToFtp(ftpServer, user, password, fileName,
                        fileStream);
            }
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
    public boolean actionPlaylist(String id) {
        try {
            String station = config.getConfig(id + ".station");
            File templateDir = new File(config.getConfig(id + ".templateDir"));
            File uploadDir = new File(config.getConfig(id + ".uploadDir"));
            String ftpServer = config.getConfig("template." + station
                    + ".ftpServer");
            String user = config.getConfig("template." + station + ".user");
            String password = config.getConfig("template." + station
                    + ".password");

            if (station == null || ftpServer == null || user == null
                    || password == null) {
                LOG.error("Configuration error for actionPlaylist('" + id
                        + "')");
                return false;
            }

            LOG.info("actionPlaylist('" + id + "')");

            TemplateUtil.processTemplates(station, templateDir, uploadDir,
                    ftpServer, user, password, titleRepository);
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean actionHttpGet(String id, String... parameters) {
        try {
            String url = config.getConfig(id + ".url");
            UriTemplate template = new UriTemplate(url);
            List<String> resolvedParameters = new ArrayList<String>();

            int i = 0;
            for (String var : template.getVariableNames()) {
                if (var.startsWith("config.id.")) {
                    resolvedParameters.add(config.getConfig(id + "."
                            + var.substring("config.id.".length())));
                } else if (var.startsWith("config.")) {
                    resolvedParameters.add(config.getConfig(var
                            .substring("config.".length())));
                } else {
                    try {
                        resolvedParameters.add(parameters[i++]);
                    } catch (IndexOutOfBoundsException e) {
                        resolvedParameters.add(null);
                    }
                }
                if (resolvedParameters.get(resolvedParameters.size() - 1) == null) {
                    LOG.error("missing variable <" + var + "> for url <" + url
                            + ">");
                    return false;
                }
            }

            LOG.info("actionHttpGet('" + id + "', ...)");

            NetUtilities.getData(restTemplate, url, resolvedParameters);
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean actionDynamicLabel(String id, String dynamicLabel) {
        try {
            String contentServer = config.getConfig(id + ".contentServer");
            String user = config.getConfig(id + ".user");
            String password = config.getConfig(id + ".password");

            if (contentServer == null || user == null || password == null) {
                LOG.error("Configuration error for actionDynamicLabel('" + id
                        + "', ...)");
                return false;
            }

            LOG.info("actionDynamicLabel('" + id + "', ...)");

            NetUtilities.updateDynamicLabel(restTemplate, contentServer, user,
                    password, dynamicLabel);
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean actionUpdateSlideshow(String id) {
        try {
            String contentServer = config.getConfig(id + ".contentServer");
            String user = config.getConfig(id + ".user");
            String password = config.getConfig(id + ".password");

            if (contentServer == null || user == null || password == null) {
                LOG.error("Configuration error for actionUpdateSlideshow('"
                        + id + "')");
                return false;
            }

            LOG.info("actionUpdateSlideshow('" + id + "')");

            NetUtilities.updateSlideshow(restTemplate, contentServer, user,
                    password);
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean actionDynamicLabelXML(String id, String label) {
        try {
            String ftpServer = config.getConfig(id + ".ftpServer");
            String user = config.getConfig(id + ".user");
            String password = config.getConfig(id + ".password");

            if (ftpServer == null || user == null || password == null) {
                LOG.error("Configuration error for actionUpdateLabelXML('" + id
                        + "')");
                return false;
            }

            LOG.info("actionUpdateLabelXML('" + id + "')");

            Document doc = createDocument(true, label);

            StringWriter strWriter = new StringWriter();
            writeDocument(doc, "iso-8859-1", strWriter);

            NetUtilities.uploadToFtp(ftpServer, user, password,
                    "DynamicLabel.xml", new ByteArrayInputStream(strWriter
                            .toString().getBytes(StandardCharsets.ISO_8859_1)));
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean actionTweet(String id, String message, String pictureUrl) {
        try {
            String consumerKey = config.getConfig(id + ".consumerKey");
            String consumerSecret = config.getConfig(id + ".consumerSecret");
            String accessToken = config.getConfig(id + ".accessToken");
            String accessTokenSecret = config.getConfig(id
                    + ".accessTokenSecret");

            if (consumerKey == null || consumerSecret == null
                    || accessToken == null || accessTokenSecret == null
                    || message == null) {
                LOG.error("Configuration error for actionTweet('" + id + "')");
                return false;
            }
            LOG.info("actionTweet('" + id + "')");

            Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret,
                    accessToken, accessTokenSecret);

            TweetData data = new TweetData(StringUtils.left(message, 140));
            if (StringUtils.trimToNull(pictureUrl) != null) {
                data.withMedia(new InputStreamResource(NetUtilities
                        .getInputStream(pictureUrl)));
            }

            int count = 0;
            Exception last = null;
            boolean done = false;
            while (count++ < 5) {
                try {
                    twitter.timelineOperations().updateStatus(data);
                    done = true;
                    break;
                } catch (Exception e) {
                    // do nothing and try again
                    last = e;
                }
            }

            if (done) {
                return true;
            }

            throw last;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    @Async
    public void executeTrigger(Trigger trigger, Title title) {
        LOG.debug("START TRIGGER ---------");

        int count = 0;
        while (locks.contains(trigger.getId())) {
            try {
                LOG.debug("SLEEP TRIGGER ---------");
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            if (count++ > 10) {
                // give up
                LOG.error("Giving up on trigger <" + trigger + ">");
                return;
            }
        }
        try {
            synchronized (locks) {
                locks.add(trigger.getId());
            }

            Expression e = expressionParser.parseExpression(trigger.getCause());

            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setVariable("title", title);

            if (e.getValue(context).equals(Boolean.TRUE)) {
                LOG.info("Trigger action : " + trigger.getAction());
                e = expressionParser.parseExpression(trigger.getAction());

                context = new StandardEvaluationContext(this);
                context.setVariable("title", title);

                e.getValue(context);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            synchronized (locks) {
                locks.remove(trigger.getId());
            }
            LOG.debug("END   TRIGGER ---------");
        }
    }

    public static void main(String[] args) throws Exception {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO); // change to debug

    }

    private static void writeDocument(Document doc, String encoding,
            Writer output) throws IOException {
        // Append formatting
        OutputFormat format = new OutputFormat(doc);

        format.setEncoding(encoding);
        format.setOmitDocumentType(false);
        format.setOmitXMLDeclaration(false);

        format.setLineWidth(0);
        format.setIndenting(true);
        format.setIndent(0);
        XMLSerializer serializer = new XMLSerializer(output, format);
        serializer.serialize(doc);
    }

    private static Document createDocument(boolean cdata, String text)
            throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();

        Element rss = doc.createElement("rss");
        rss.setAttribute("version", "2.0");

        Element channel = doc.createElement("channel");
        rss.appendChild(channel);

        Element description = doc.createElement("description");
        channel.appendChild(description);

        createTitle(doc, channel, text, cdata);

        doc.appendChild(rss);
        return doc;
    }

    private static void createTitle(Document doc, Element channel,
            String title, boolean cdata) {
        Element item = doc.createElement("item");
        Element tit = doc.createElement("title");
        item.appendChild(tit);

        if (cdata) {
            CDATASection cdat = doc.createCDATASection(title);
            tit.appendChild(cdat);
        } else {
            tit.setTextContent(title);
        }
        channel.appendChild(item);
    }
}
