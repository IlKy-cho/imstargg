package com.imstargg.storage.db.core.brawlstars;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BrawlerCollectionEntityImage {

    @Nullable
    @Column(name = "image_url", length = 500, updatable = false, nullable = false)
    private String url;

    protected BrawlerCollectionEntityImage() {
    }

    public BrawlerCollectionEntityImage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
