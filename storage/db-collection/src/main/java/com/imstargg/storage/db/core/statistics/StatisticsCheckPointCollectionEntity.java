package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;

@Entity
@Table(
        name = "stats_checkpoint",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_stats_checkpoint__statskey_battledate",
                        columnNames = {"stats_key", "battle_date"}
                )
        }
)
public class StatisticsCheckPointCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stats_checkpoint_id")
    private Long id;

    @Column(name = "stats_key", nullable = false, updatable = false)
    private String key;

    @Column(name = "battle_date", nullable = false, updatable = false)
    private LocalDate battleDate;

    @Column(name = "battle_id", nullable = false)
    private long battleId = 0;

    protected StatisticsCheckPointCollectionEntity() {
    }

    public StatisticsCheckPointCollectionEntity(String key, LocalDate battleDate) {
        this.key = key;
        this.battleDate = battleDate;
    }

    public void updateBattleId(long battleId) {
        if (battleId <= this.battleId) {
            return;
        }
        this.battleId = battleId;
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public LocalDate getBattleDate() {
        return battleDate;
    }

    public long getBattleId() {
        return battleId;
    }
}
