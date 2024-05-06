package com.github.pavelvil.eventsapi.web.advice;

import com.github.pavelvil.eventsapi.exception.AccessDeniedException;
import com.github.pavelvil.eventsapi.exception.EntityNotFoundException;
import com.github.pavelvil.eventsapi.web.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalRestControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNofFoundHandler(EntityNotFoundException ex) {
        log.error("GlobalRestControllerAdvice -> entityNofFoundHandler", ex);
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedHandler(AccessDeniedException ex) {
        log.error("GlobalRestControllerAdvice -> accessDeniedHandler", ex);
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
        log.error("GlobalRestControllerAdvice -> exceptionHandler", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ErrorResponse.builder()
                .message(message)
                .build());
    }

}
