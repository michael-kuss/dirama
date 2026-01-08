/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.controller.api;

import de.miq.dirama.common.OpenApiConstants;
import de.miq.dirama.common.TimeHelper;
import de.miq.dirama.dto.title.TitleRequest;
import de.miq.dirama.dto.title.TitleResponse;
import de.miq.dirama.service.TitleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nowplaying")
@SecurityRequirement(name = OpenApiConstants.BASIC_SECURITY_REQUIREMENT)
@Slf4j
public class NowPlayingController {

  private final TitleService titleService;

  public NowPlayingController(TitleService titleService) {
    this.titleService = titleService;
  }

  @PreAuthorize("isAuthenticated()")
  @ResponseStatus(value = HttpStatus.CREATED)
  @PostMapping("/{station}")
  public TitleResponse addNowPlaying(
      @PathVariable("station") String station,
      @RequestParam("artist") String artist,
      @RequestParam("title") String title,
      @RequestParam("dabImage") String dabImage,
      @RequestParam("webImage") String webImage,
      @RequestParam("time") String time,
      @RequestParam(value = "additional1", required = false) String additional1,
      @RequestParam(value = "additional2", required = false) String additional2,
      @RequestParam(value = "additional3", required = false) String additional3,
      @RequestParam(value = "additional4", required = false) String additional4,
      @RequestParam(value = "additional5", required = false) String additional5,
      @RequestParam(value = "ignoreNow", defaultValue = "false") boolean ignoreNow,
      @RequestParam(value = "trigger", defaultValue = "true") boolean trigger) {

    TitleRequest titleRequest;
    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime dateTime = TimeHelper.convert(time);

    if (!ignoreNow) {
      ZonedDateTime before = now.minus(10, ChronoUnit.MINUTES);
      ZonedDateTime after = now.plus(10, ChronoUnit.MINUTES);

      if (!(dateTime.isAfter(after) && dateTime.isBefore(before))) {
        throw new IllegalStateException("Date not now");
      }
    }

    titleRequest =
        new TitleRequest(
            artist,
            title,
            dabImage,
            webImage,
            dateTime,
            additional1,
            additional2,
            additional3,
            additional4,
            additional5);

    // titleRepository.index(titleRequest);
    log.info("Added {}", titleRequest);

    if (trigger) {
      // triggerService.executeTriggers(titleRequest);
    }
    return titleService.createTitle(station, titleRequest);
  }
}
