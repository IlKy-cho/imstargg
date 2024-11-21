package com.imstargg.client.brawlstars;

import feign.FeignException;

public class BrawlstarsClientNotFoundException extends RuntimeException {

    public BrawlstarsClientNotFoundException(String message, FeignException.NotFound ex) {
        super(message, ex);
    }
}
