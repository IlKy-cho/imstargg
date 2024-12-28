package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "brawlers_winning")
public class BrawlersWinningCollectionEntity extends BrawlerWinningBaseCollectionEntity {

    @Id
    @Column(name = "brawlers_winning_id")
    private Long id;

    @Column(name = "brawler_num", updatable = false, nullable = false)
    private int brawlerNum;

    @Column(name = "brawler_brawlstars_id_hash", length = 60, updatable = false, nullable = false)
    private String brawlerBrawlStarsIdHash;

    protected BrawlersWinningCollectionEntity() {
    }

    public BrawlersWinningCollectionEntity(
            @Nullable SoloRankTierRange soloRankTierRange,
            @Nullable TrophyRange trophyRange,
            long battleEventId,
            LocalDate battleDate,
            long brawlerBrawlStarsId,
            int brawlerNum,
            String brawlerBrawlStarsIdHash
    ) {
        super(soloRankTierRange, trophyRange, battleEventId, battleDate, brawlerBrawlStarsId);
        this.brawlerNum = brawlerNum;
        this.brawlerBrawlStarsIdHash = brawlerBrawlStarsIdHash;
    }
}
