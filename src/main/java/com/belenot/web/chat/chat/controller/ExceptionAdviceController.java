package com.belenot.web.chat.chat.controller;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionAdviceController extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public HttpEntity<String> handleValidationException(ConstraintViolationException exc) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        String description = exc.getConstraintViolations().stream().map(v -> v.getMessage()).reduce("Violations: ",
                (ac, msg) -> ac += msg);
        ResponseEntity<String> entity = new ResponseEntity<String>(description, HttpStatus.BAD_REQUEST);

        return entity;
    }

    // @ExceptionHandler
    // public HttpEntity<String> handleBindException(BindException exc) {
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setContentType(MediaType.TEXT_PLAIN);
    //     String description = exc.getMessage();
    //     ResponseEntity<String> entity = new ResponseEntity<String>(description, HttpStatus.BAD_REQUEST);

    //     return entity;
    // }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        String description = ex.getMessage();
        ResponseEntity<Object> entity = new ResponseEntity<>(description, HttpStatus.BAD_REQUEST);
        return entity;
    }

    



}