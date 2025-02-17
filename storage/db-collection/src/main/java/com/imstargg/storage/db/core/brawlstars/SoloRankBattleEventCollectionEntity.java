package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "solo_rank_battle_event")
public class SoloRankBattleEventCollectionEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solo_rank_battle_event_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "event_brawlstars_id", referencedColumnName = "brawlstars_id", updatable = false, nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private BattleEventCollectionEntity event;

    protected SoloRankBattleEventCollectionEntity() {
    }

    public SoloRankBattleEventCollectionEntity(BattleEventCollectionEntity event) {
        this.event = event;
    }

    public Long getId() {
        return id;
    }

    public BattleEventCollectionEntity getEvent() {
        return event;
    }
}
