package com.imstargg.core.enums;

public enum ImageType {

    BRAWLER_PROFILE(ImageBucket.BRAWL_STARS),
    ;

    private final ImageBucket bucket;

    ImageType(ImageBucket bucket) {
        this.bucket = bucket;
    }

    public ImageBucket getBucket() {
        return bucket;
    }
}
