package com.imstargg.client.core;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class DefaultClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        Exception exception = defaultErrorDecoder.decode(methodKey, response);
        if (exception instanceof RetryableException) {
            return exception;
        }

        HttpStatus status = HttpStatus.valueOf(response.status());
        if (status.is5xxServerError() || status == HttpStatus.TOO_MANY_REQUESTS) {
            return new RetryableException(
                    response.status(),
                    exception.getMessage(),
                    response.request().httpMethod(),
                    exception,
                    (Long) null,
                    response.request());
        }

        return exception;
    }
}
