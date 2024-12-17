package com.imstargg.support.aws.s3;

import org.springframework.core.io.Resource;

import java.io.IOException;

public class NotSupportedImageException extends RuntimeException {

    NotSupportedImageException(Resource resource) {
        super("resource=" + resource);
    }

    NotSupportedImageException(Resource resource, IOException e) {
        super("resource=" + resource, e);
    }
}
