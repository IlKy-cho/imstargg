package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

@MappedSuperclass
abstract class BrawlerBattleResultStatisticsBaseCollectionEntity extends BattleStatisticsBaseCollectionEntity {

    @Column(name = "victory_count", nullable = false)
    private long victoryCount;

    @Column(name = "defeat_count", nullable = false)
    private long defeatCount;

    @Column(name = "draw_count", nullable = false)
    private long drawCount;

    protected BrawlerBattleResultStatisticsBaseCollectionEntity() {
    }

    protected BrawlerBattleResultStatisticsBaseCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            TrophyRange trophyRange,
            LocalDate battleDate
    ) {
        super(eventBrawlStarsId, brawlerBrawlStarsId, trophyRange, battleDate);
        this.victoryCount = 0;
        this.defeatCount = 0;
        this.drawCount = 0;
    }

    protected BrawlerBattleResultStatisticsBaseCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            SoloRankTierRange soloRankTierRange,
            LocalDate battleDate
    ) {
        super(eventBrawlStarsId, brawlerBrawlStarsId, soloRankTierRange, battleDate);
        this.victoryCount = 0;
        this.defeatCount = 0;
        this.drawCount = 0;
    }

    protected BrawlerBattleResultStatisticsBaseCollectionEntity(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            LocalDate battleDate
    ) {
        super(eventBrawlStarsId, brawlerBrawlStarsId, battleDate);
        this.victoryCount = 0;
        this.defeatCount = 0;
        this.drawCount = 0;
    }

    public void countUp(BattleResult result) {
        switch (result) {
            case VICTORY:
                victoryCount++;
                break;
            case DEFEAT:
                defeatCount++;
                break;
            case DRAW:
                drawCount++;
                break;
        }
    }

    public long getVictoryCount() {
        return victoryCount;
    }

    public long getDefeatCount() {
        return defeatCount;
    }

    public long getDrawCount() {
        return drawCount;
    }
}
