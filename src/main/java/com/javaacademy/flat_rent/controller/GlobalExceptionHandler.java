package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.exception.EntityNotFoundException;
import com.javaacademy.flat_rent.exception.IntersectionDateException;
import com.javaacademy.flat_rent.exception.NotActiveAdvertException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotActiveAdvertException.class)
    public ResponseEntity<?> handle400exception(RuntimeException e) {
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handle404exception(RuntimeException e) {
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IntersectionDateException.class)
    public ResponseEntity<?> handle409Exception(RuntimeException e) {
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
}
