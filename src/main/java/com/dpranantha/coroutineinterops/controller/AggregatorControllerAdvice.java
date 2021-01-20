package com.dpranantha.coroutineinterops.controller;

import com.dpranantha.coroutineinterops.controller.exception.ErrorMessage;
import com.dpranantha.coroutineinterops.controller.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice()
public class AggregatorControllerAdvice {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> handleProductNotFound(Exception ex) {
        return new ResponseEntity<>(new ErrorMessage(ex.getCause().getLocalizedMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }
}
