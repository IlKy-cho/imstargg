package com.imstargg.storage.db.core.statistics;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "battle_rank_statistics_collected",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_battlekey_rank", columnNames = {"battle_key", "rank_value"})
        }
)
public class BattleRankStatisticsCollectedCollectionEntity extends BaseEntity {

    @Id
    @Column(name = "statistics_collected_id")
    private Long id;

    @Column(name = "battle_key", updatable = false, nullable = false)
    private String battleKey;

    @Column(name = "rank_value", updatable = false, nullable = false)
    private int rank;

    protected BattleRankStatisticsCollectedCollectionEntity() {
    }

    public BattleRankStatisticsCollectedCollectionEntity(String battleKey, int rank) {
        this.battleKey = battleKey;
        this.rank = rank;
    }

    public Long getId() {
        return id;
    }

    public String getBattleKey() {
        return battleKey;
    }

    public int getRank() {
        return rank;
    }
}
