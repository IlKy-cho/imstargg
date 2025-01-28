package com.imstargg.client.brawlstars;

import feign.FeignException;

public class BrawlStarsClientException extends RuntimeException {

    BrawlStarsClientException(String message, FeignException ex) {
        super(message, ex);
    }

    BrawlStarsClientException(String message) {
        super(message);
    }

    private FeignException getFeignException() {
        return ((FeignException) getCause());
    }

    public static class NotFound extends BrawlStarsClientException {

        public NotFound(String message, FeignException.NotFound ex) {
            super(message, ex);
        }
    }

    public static class InMaintenance extends BrawlStarsClientException {

        public InMaintenance(String message) {
            super(message);
        }
    }
}
