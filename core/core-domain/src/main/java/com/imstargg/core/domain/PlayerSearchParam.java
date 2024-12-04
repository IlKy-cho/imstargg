package com.imstargg.core.domain;

public record PlayerSearchParam(String query) {

    public boolean isTag() {
        return query.startsWith("#");
    }

    public BrawlStarsTag toTag() {
        return new BrawlStarsTag(query);
    }

    public BrawlStarsId toId() {
        return new BrawlStarsId(Long.parseLong(query));
    }
}
