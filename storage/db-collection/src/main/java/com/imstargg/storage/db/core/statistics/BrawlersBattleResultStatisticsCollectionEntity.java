package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "brawlers_battle_result_stats")
public class BrawlersBattleResultStatisticsCollectionEntity extends BrawlerBattleResultStatisticsBaseCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawlers_battle_result_stats_id")
    private Long id;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Embedded
    private BattleStatisticsCollectionEntityBrawlers brawlers;

    protected BrawlersBattleResultStatisticsCollectionEntity() {
    }

    public BrawlersBattleResultStatisticsCollectionEntity(
            long battleEventId,
            LocalDate battleDate,
            @Nullable SoloRankTierRange soloRankTierRange,
            @Nullable TrophyRange trophyRange,
            boolean duplicateBrawler,
            long brawlerBrawlStarsId,
            BattleStatisticsCollectionEntityBrawlers brawlers
    ) {

        super(battleEventId, battleDate, soloRankTierRange, trophyRange, duplicateBrawler);
        this.brawlerBrawlStarsId = brawlerBrawlStarsId;
        this.brawlers = brawlers;
    }

    public Long getId() {
        return id;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }

    public BattleStatisticsCollectionEntityBrawlers getBrawlers() {
        return brawlers;
    }
}
