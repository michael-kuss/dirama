package de.miq.dirama.server.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.miq.dirama.server.model.Title;
import de.miq.dirama.server.model.Trigger;
import de.miq.dirama.server.repository.TitleRepository;
import de.miq.dirama.server.repository.TriggerRepository;
import de.miq.dirama.server.services.TriggerActionService;

@Controller
@RequestMapping("/nowplaying")
public class NowPlayingController {
    private static final Log LOG = LogFactory
            .getLog(NowPlayingController.class);

    private static final DateFormat DATEFORMAT = new SimpleDateFormat(
            "yyyyMMddHHmmss");

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private TriggerActionService triggerService;

    private ExpressionParser expressionParser = new SpelExpressionParser();

    @RequestMapping(method = RequestMethod.POST, value = { "/{station}" })
    @ResponseBody
    public ResponseEntity<List<String>> addNowPlaying(
            @PathVariable("station") String station,
            @RequestParam("artist") String artist,
            @RequestParam("title") String title,
            @RequestParam("dabImage") String dabImage,
            @RequestParam("webImage") String webImage,
            @RequestParam("time") String time) {
        Title tt;
        try {
            Date date = DATEFORMAT.parse(time);
            tt = new Title(station, artist, title, dabImage, webImage, date);

            titleRepository.index(tt);
            LOG.info("Added " + tt);

            executeTriggers(tt);
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    private void executeTriggers(Title title) {
        Iterable<Trigger> triggers = triggerRepository.findAll();

        LOG.info("triggers...");
        for (Trigger trigger : triggers) {
            Expression e = expressionParser.parseExpression(trigger.getCause());

            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setVariable("title", title);

            if (e.getValue(context).equals(Boolean.TRUE)) {
                LOG.info("Trigger action : " + trigger.getAction());
                e = expressionParser.parseExpression(trigger.getAction());

                context = new StandardEvaluationContext(triggerService);
                context.setVariable("title", title);

                e.getValue(context);
            }
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = { "/{station}" })
    @ResponseBody
    public List<Title> requestNowPlaying(
            @PathVariable("station") String station,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        Pageable pageable = new PageRequest(0, size, new Sort(Direction.DESC,
                "time"));
        Page<Title> titles = titleRepository.findByStation(station, pageable);
        if (titles == null) {
            return null;
        }

        return titles.getContent();
    }
}
