package com.imstargg.support.aws.s3;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

class ResourceImage implements Image {

    private final Resource resource;

    private final String filename;

    private final ImageContentType contentType;

    ResourceImage(Resource resource) {
        this.resource = resource;
        this.filename = resource.getFilename();
        if (this.filename == null) {
            throw new NotSupportedImageException(resource);
        }

        try {
            this.contentType = new ImageContentType(
                    Files.probeContentType(Paths.get(Objects.requireNonNull(this.filename)))
            );
        } catch (IOException e) {
            throw new NotSupportedImageException(resource, e);
        }
    }

    @Override
    public String ext() {
        int pos = this.filename.lastIndexOf(".");
        return this.filename.substring(pos + 1);
    }

    @Override
    public ImageContentType contentType() {
        return contentType;
    }

    @Override
    public long contentLength() throws IOException {
        return resource.contentLength();
    }

    @Override
    public InputStream inputStream() throws IOException {
        return resource.getInputStream();
    }

    @Override
    public String toString() {
        return "ResourceImage{" +
                "resource=" + resource +
                '}';
    }
}
