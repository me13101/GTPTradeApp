package com.gtp.tradeapp.rest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    private static Logger LOGGER = Logger.getLogger(ExceptionHandlerControllerAdvice.class);

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.info("Error {}", e);
        return e.getMessage();
    }
}
