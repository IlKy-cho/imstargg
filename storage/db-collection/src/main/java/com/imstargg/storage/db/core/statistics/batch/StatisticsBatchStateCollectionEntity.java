package com.imstargg.storage.db.core.statistics.batch;

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
        name = "statistics_batch_state",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_statistics_batch_state__key",
                        columnNames = {"job_name", "season_number", "event_brawlstars_id"}
                )
        }
)
public class StatisticsBatchStateCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistics_batch_state_id")
    private Long id;

    @Column(name = "job_name", length = 105, updatable = false, nullable = false)
    private String jobName;

    @Column(name = "season_number", updatable = false, nullable = false)
    private int seasonNumber;

    @Column(name = "event_brawlstars_id", updatable = false, nullable = false)
    private long eventBrawlStarsId;

    @Column(name = "last_processed_battle_id")
    private Long lastProcessedBattleId;

    protected StatisticsBatchStateCollectionEntity() {
    }

    public StatisticsBatchStateCollectionEntity(String jobName, int seasonNumber, long eventBrawlStarsId) {
        this.jobName = jobName;
        this.seasonNumber = seasonNumber;
        this.eventBrawlStarsId = eventBrawlStarsId;
    }

    public void update(Long lastProcessedBattleId) {
        this.lastProcessedBattleId = lastProcessedBattleId;
    }

    public Long getId() {
        return id;
    }

    public String getJobName() {
        return jobName;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public long getEventBrawlStarsId() {
        return eventBrawlStarsId;
    }

    public Long getLastProcessedBattleId() {
        return lastProcessedBattleId;
    }
}
