package de.miq.dirama.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionHandlerController {
    private static final Log LOG = LogFactory
            .getLog(ExceptionHandlerController.class);

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleError(HttpServletRequest request,
            HttpServletResponse response, Exception e) {
        HttpStatus status = HttpStatus.valueOf(response.getStatus());

        LOG.error(status + " : " + e.getMessage(), e);

        return new JsonError(request.getRequestURL().toString(), status, e)
                .asModelAndView();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ModelAndView handle404(HttpServletRequest request,
            HttpServletResponse response, Exception e) {
        return new JsonError(request.getRequestURL().toString(),
                HttpStatus.NOT_FOUND, null).asModelAndView();
    }
}
