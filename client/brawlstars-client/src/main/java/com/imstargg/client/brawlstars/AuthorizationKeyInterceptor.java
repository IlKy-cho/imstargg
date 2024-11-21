package com.imstargg.client.brawlstars;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;

class AuthorizationKeyInterceptor implements RequestInterceptor {

    private final String key;

    public AuthorizationKeyInterceptor(String key) {
        this.key = key;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + key);
    }
}
