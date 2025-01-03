package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "seasoned_battle_event")
public class SeasonedBattleEventCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seasoned_battle_event_id")
    private Long id;

    @JoinColumn(name = "battle_event_id", updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private BattleEventCollectionEntity battleEvent;

    protected SeasonedBattleEventCollectionEntity() {
    }

    public SeasonedBattleEventCollectionEntity(BattleEventCollectionEntity battleEvent) {
        this.battleEvent = battleEvent;
    }

    public Long getId() {
        return id;
    }

    public BattleEventCollectionEntity getBattleEvent() {
        return battleEvent;
    }
}
