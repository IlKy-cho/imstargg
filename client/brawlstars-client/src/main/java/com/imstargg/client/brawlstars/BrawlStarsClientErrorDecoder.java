package com.imstargg.client.brawlstars;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imstargg.client.core.DefaultClientErrorDecoder;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

public class BrawlStarsClientErrorDecoder implements ErrorDecoder {

    private final DefaultClientErrorDecoder defaultErrorDecoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BrawlStarsClientErrorDecoder(DefaultClientErrorDecoder defaultErrorDecoder) {
        this.defaultErrorDecoder = defaultErrorDecoder;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        if (isInMaintenance(response)) {
            return new BrawlStarsClientException.InMaintenance("현재 서비스 점검 중입니다.");

        }
        return defaultErrorDecoder.decode(methodKey, response);
    }

    private boolean isInMaintenance(Response response) {
        return response.status() == HttpStatus.SERVICE_UNAVAILABLE.value() &&
                Optional.ofNullable(response.body())
                        .map(body -> {
                            try {
                                return objectMapper.readValue(body.asInputStream(),
                                        BrawlStarsClientErrorResponse.class);
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        })
                        .filter(BrawlStarsClientErrorResponse::isInMaintenance)
                        .isPresent();
    }
}
