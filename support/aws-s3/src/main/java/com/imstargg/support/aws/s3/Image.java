package com.imstargg.support.aws.s3;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

public interface Image {

    String ext();

    ImageContentType contentType() throws IOException;

    long contentLength() throws IOException;

    InputStream inputStream() throws IOException;

    static Image resource(Resource resource) {
        return new ResourceImage(resource);
    }
}
