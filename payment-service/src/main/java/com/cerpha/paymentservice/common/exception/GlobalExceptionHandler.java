package com.cerpha.paymentservice.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

import static com.cerpha.paymentservice.common.exception.ExceptionCode.INVALID_REQUEST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> businessExceptionHandler(BusinessException e) {
        log.error("BusinessExceptionHandler", e);

        return ResponseEntity.status(e.getExceptionCode().getStatus()).body(new ExceptionResponse(e.getExceptionCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        List<InvalidError> errors = e.getFieldErrors().stream()
                .map(error -> InvalidError.builder()
                        .fieldName(error.getField())
                        .rejectValue(String.valueOf(error.getRejectedValue()))
                        .message(error.getDefaultMessage())
                        .build())
                .toList();

        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse(INVALID_REQUEST, errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception e) {
        log.error("ERROR", e);
        return ResponseEntity.status(400)
                .body(e);
    }
}
