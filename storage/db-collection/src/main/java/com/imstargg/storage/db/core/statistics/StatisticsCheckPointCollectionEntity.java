package com.imstargg.storage.db.core.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "stats_checkpoint",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_stats_checkpoint__statskey",
                        columnNames = {"stats_key"}
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

    @Column(name = "last_battle_id", nullable = false)
    private long lastBattleId = 0;

    protected StatisticsCheckPointCollectionEntity() {
    }

    public StatisticsCheckPointCollectionEntity(String key) {
        this.key = key;
    }

    public void updateLastBattleId(long lastBattleId) {
        this.lastBattleId = lastBattleId;
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public long getLastBattleId() {
        return lastBattleId;
    }
}
