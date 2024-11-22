package com.imstargg.client.brawlstars;

import feign.FeignException;

public class BrawlStarsClientNotFoundException extends RuntimeException {

    public BrawlStarsClientNotFoundException(String message, FeignException.NotFound ex) {
        super(message, ex);
    }
}
