package com.imstargg.admin.controller.error;


import com.imstargg.admin.support.error.AdminErrorKind;
import com.imstargg.admin.support.error.AdminException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(AdminException.class)
    private ResponseEntity<ErrorResponse> handleCoreException(AdminException ex) {
        AdminErrorKind errorKind = ex.getErrorKind();
        if (AdminErrorKind.SERVER_ERROR == errorKind) {
            log.error("{} : {}", AdminException.class.getSimpleName(), ex.getMessage(), ex);
        } else {
            log.info("{} : {}", AdminException.class.getSimpleName(), ex.getMessage(), ex);
        }
        HttpStatus status = getHttpStatusCode(errorKind);
        ErrorResponse response = new ErrorResponse(errorKind, ex.getMessage());
        return ResponseEntity
                .status(status)
                .body(response);
    }

    private HttpStatus getHttpStatusCode(AdminErrorKind kind) {
        return switch (kind) {
            case AdminErrorKind.SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            case AdminErrorKind.UNAVAILABLE -> HttpStatus.SERVICE_UNAVAILABLE;
            case AdminErrorKind.VALIDATION_FAILED, AdminErrorKind.ILLEGAL_STATE -> HttpStatus.BAD_REQUEST;
            case AdminErrorKind.NOT_FOUND -> HttpStatus.NOT_FOUND;
            case AdminErrorKind.DUPLICATED -> HttpStatus.CONFLICT;
            case AdminErrorKind.FORBIDDEN -> HttpStatus.FORBIDDEN;
        };
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("{} : {}", Exception.class.getSimpleName(), ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(AdminErrorKind.SERVER_ERROR, ex.getMessage()));
    }

}
