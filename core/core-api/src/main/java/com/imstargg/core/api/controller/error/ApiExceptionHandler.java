package com.imstargg.core.api.controller.error;


import com.imstargg.core.domain.CoreErrorKind;
import com.imstargg.core.domain.CoreErrorType;
import com.imstargg.core.domain.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(CoreException.class)
    private ResponseEntity<ErrorResponse> handleCoreException(CoreException ex) {
        CoreErrorType errorType = ex.getErrorType();
        switch (errorType.getLevel()) {
            case ERROR -> log.error("{} : {}", CoreException.class.getSimpleName(), ex.getMessage(), ex);
            case WARN -> log.warn("{} : {}", CoreException.class.getSimpleName(), ex.getMessage(), ex);
            case INFO -> log.info("{} : {}", CoreException.class.getSimpleName(), ex.getMessage(), ex);
        }
        HttpStatus status = getHttpStatusCode(errorType.getKind());
        ErrorResponse response = ErrorResponse.of(errorType);
        return ResponseEntity
                .status(status)
                .body(response);
    }

    private HttpStatus getHttpStatusCode(CoreErrorKind kind) {
        return switch (kind) {
            case CoreErrorKind.SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            case CoreErrorKind.UNAVAILABLE -> HttpStatus.SERVICE_UNAVAILABLE;
            case CoreErrorKind.VALIDATION_FAILED, CoreErrorKind.ILLEGAL_STATE -> HttpStatus.BAD_REQUEST;
            case CoreErrorKind.NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CoreErrorKind.DUPLICATED -> HttpStatus.CONFLICT;
            case CoreErrorKind.FORBIDDEN -> HttpStatus.FORBIDDEN;
        };
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("{} : {}", Exception.class.getSimpleName(), ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(CoreErrorType.DEFAULT_ERROR));
    }

}
