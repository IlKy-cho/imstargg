package com.imstargg.support.aws.s3;


import jakarta.annotation.Nullable;

import java.util.Map;

public record ImageContentType(
        @Nullable String value
) {

    private static final Map<String, String> TYPE_TO_EXT = Map.of(
            "image/jpeg", "jpeg",
            "image/png", "png",
            "image/gif", "gif",
            "image/bmp", "bmp",
            "image/webp", "webp",
            "image/svg+xml", "svg",
            "image/tiff", "tif",
            "image/x-icon", "ico",
            "image/vnd.microsoft.icon", "ico"
    );

    public boolean supports() {
        if (value == null) {
            return false;
        }
        return TYPE_TO_EXT.containsKey(value.split(";")[0]);
    }

    public String ext() {
        return TYPE_TO_EXT.get(value);
    }
}
