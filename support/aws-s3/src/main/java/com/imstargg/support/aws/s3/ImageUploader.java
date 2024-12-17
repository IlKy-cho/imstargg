package com.imstargg.support.aws.s3;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;

@Component
public class ImageUploader {

    private final S3Template s3Template;

    public ImageUploader(S3Template s3Template) {
        this.s3Template = s3Template;
    }

    public URL upload(Image image, String bucketName, String storedName) {
        try (InputStream input = image.inputStream()) {

            ObjectMetadata metadata = ObjectMetadata.builder()
                    .contentType(image.contentType().value())
                    .contentLength(image.contentLength())
                    .build();
            S3Resource s3Resource = s3Template.upload(bucketName, storedName, input, metadata);
            return s3Resource.getURL();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
