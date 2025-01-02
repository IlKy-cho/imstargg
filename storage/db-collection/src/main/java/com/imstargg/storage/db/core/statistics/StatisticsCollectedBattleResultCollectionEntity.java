package com.imstargg.storage.db.core.statistics;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "stats_collected_battle_result",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_battlekey", columnNames = {"battle_key"})
        }
)
public class StatisticsCollectedBattleResultCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stats_collected_battle_result_id")
    private Long id;

    @Column(name = "battle_key", updatable = false, nullable = false)
    private String battleKey;

    protected StatisticsCollectedBattleResultCollectionEntity() {
    }

    public StatisticsCollectedBattleResultCollectionEntity(String battleKey) {
        this.battleKey = battleKey;
    }

    public Long getId() {
        return id;
    }

    public String getBattleKey() {
        return battleKey;
    }
}
