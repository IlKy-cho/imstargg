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
        name = "stats_collected_battle",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_stats_collected_battle__statskey",
                        columnNames = {"stats_key"}
                )
        }
)
public class StatisticsCollectedBattleCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stats_collected_battle_id")
    private Long id;

    @Column(name = "stats_key", nullable = false, updatable = false)
    private String key;

    @Column(name = "last_collected_id", nullable = false)
    private long lastCollectedId = 0;

    protected StatisticsCollectedBattleCollectionEntity() {
    }

    public StatisticsCollectedBattleCollectionEntity(String key) {
        this.key = key;
    }

    public void updateLastCollectedId(long lastCollectedId) {
        this.lastCollectedId = lastCollectedId;
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public long getLastCollectedId() {
        return lastCollectedId;
    }
}
