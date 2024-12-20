package com.imstargg.client.brawlstars.news;

public class BrawlStarsNewsClientException extends RuntimeException {

    public BrawlStarsNewsClientException(Exception exception) {
        super(exception);
    }

    public BrawlStarsNewsClientException(String message) {
        super(message);
    }
}
