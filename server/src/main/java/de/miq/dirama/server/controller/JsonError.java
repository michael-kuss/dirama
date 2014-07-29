package de.miq.dirama.server.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * Helper class to get json error messages.
 * 
 * @author mkuss
 * 
 */
public class JsonError {
    private final Exception exception;
    private HttpStatus status;
    private String url;

    /**
     * Construct a json error message.
     * 
     * @param message
     *            the message
     */
    public JsonError(String url, final HttpStatus status,
            final Exception exception) {
        this.url = url;
        this.exception = exception;
        this.status = status;
    }

    /**
     * Create a json error message.
     * 
     * @return The error message.
     */
    public ModelAndView asModelAndView() {
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        Map<String, String> error = new HashMap<String, String>();
        if (error != null) {
            error.put("error", exception.toString());
        }
        if (url != null) {
            error.put("url", url);
        }
        error.put("status", status.value() + "");
        error.put("status_name", status.name());
        
        return new ModelAndView(jsonView, error);
    }
}
