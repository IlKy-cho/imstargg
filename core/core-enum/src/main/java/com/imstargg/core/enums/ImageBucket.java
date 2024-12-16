package com.imstargg.core.enums;

public enum ImageBucket {

    BRAWL_STARS("brawlstars"),
    ;

    private final String name;

    ImageBucket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
