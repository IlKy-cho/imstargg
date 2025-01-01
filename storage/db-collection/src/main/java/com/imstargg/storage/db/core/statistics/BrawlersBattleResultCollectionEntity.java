package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "brawlers_battle_result")
public class BrawlersBattleResultCollectionEntity extends BrawlerBattleResultBaseCollectionEntity {

    @Id
    @Column(name = "brawlers_winning_id")
    private Long id;

    @Embedded
    private BattleStatisticsCollectionEntityBrawlers brawlers;

    protected BrawlersBattleResultCollectionEntity() {
    }

    public BrawlersBattleResultCollectionEntity(
            @Nullable SoloRankTierRange soloRankTierRange,
            @Nullable TrophyRange trophyRange,
            long battleEventId,
            LocalDate battleDate,
            long brawlerBrawlStarsId,
            boolean duplicateBrawler,
            BattleStatisticsCollectionEntityBrawlers brawlers
    ) {
        super(soloRankTierRange, trophyRange, battleEventId, battleDate, brawlerBrawlStarsId, duplicateBrawler);
        this.brawlers = brawlers;
    }

    public Long getId() {
        return id;
    }

    public BattleStatisticsCollectionEntityBrawlers getBrawlers() {
        return brawlers;
    }
}
