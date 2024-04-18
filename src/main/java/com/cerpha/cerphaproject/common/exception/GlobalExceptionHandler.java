package com.cerpha.cerphaproject.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity businessExceptionHandler(BusinessException e) {
        log.error("BusinessExceptionHandler = {}", e);

        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse(e.getExceptionCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception e) {
        return ResponseEntity.status(400)
                .body(e);
    }
}
