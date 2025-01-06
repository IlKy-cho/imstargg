package com.imstargg.client.brawlstars.news;

import feign.FeignException;

public class BrawlStarsNewsClientException extends RuntimeException {

    public BrawlStarsNewsClientException(Exception exception) {
        super(exception);
    }

    public BrawlStarsNewsClientException(String message) {
        super(message);
    }

    public static BrawlStarsNewsClientException of(Exception exception) {
        if (exception instanceof FeignException.NotFound notFoundException) {
            return new NotFound(notFoundException);
        }
        return new BrawlStarsNewsClientException(exception);
    }

    public static class NotFound extends BrawlStarsNewsClientException {

        public NotFound(FeignException.NotFound exception) {
            super(exception);
        }
    }
}
