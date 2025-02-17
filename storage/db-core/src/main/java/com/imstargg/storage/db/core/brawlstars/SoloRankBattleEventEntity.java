package com.imstargg.storage.db.core.brawlstars;

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
        name = "solo_rank_battle_event",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_solo_rank_battle_event__key", columnNames = {"event_brawlstars_id"})
        }
)
public class SoloRankBattleEventEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solo_rank_battle_event_id")
    private Long id;

    @Column(name = "event_brawlstars_id", updatable = false, nullable = false)
    private long eventBrawlStarsId;

    protected SoloRankBattleEventEntity() {
    }

    public Long getId() {
        return id;
    }

    public long getEventBrawlStarsId() {
        return eventBrawlStarsId;
    }
}
