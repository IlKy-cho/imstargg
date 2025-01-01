package com.imstargg.storage.db.core.statistics;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "battle_result_statistics_collected",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_battlekey", columnNames = {"battle_key"})
        }
)
public class BattleResultStatisticsCollectedCollectionEntity extends BaseEntity {

    @Id
    @Column(name = "statistics_collected_id")
    private Long id;

    @Column(name = "battle_key", updatable = false, nullable = false)
    private String battleKey;

    protected BattleResultStatisticsCollectedCollectionEntity() {
    }

    public BattleResultStatisticsCollectedCollectionEntity(String battleKey) {
        this.battleKey = battleKey;
    }

    public Long getId() {
        return id;
    }

    public String getBattleKey() {
        return battleKey;
    }
}
