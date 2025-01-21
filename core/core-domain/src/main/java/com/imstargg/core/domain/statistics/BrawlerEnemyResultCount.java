package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BrawlerEnemyResultCount(
        BrawlStarsId brawlerId,
        BrawlStarsId enemyBrawlerId,
        ResultCount resultCount
) {

    public BrawlerEnemyResultCount merge(BrawlerEnemyResultCount other) {
        if (!brawlerId.equals(other.brawlerId)) {
            throw new IllegalArgumentException(
                    "Brawler ID is not matched. " + brawlerId + " != " + other.brawlerId);
        }
        if (!enemyBrawlerId.equals(other.enemyBrawlerId)) {
            throw new IllegalArgumentException(
                    "Enemy Brawler ID is not matched. " + enemyBrawlerId + " != " + other.enemyBrawlerId);
        }

        return new BrawlerEnemyResultCount(
                brawlerId,
                enemyBrawlerId,
                resultCount.merge(other.resultCount)
        );
    }
}
