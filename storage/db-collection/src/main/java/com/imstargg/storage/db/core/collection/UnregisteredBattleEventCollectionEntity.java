package com.imstargg.storage.db.core.collection;

import com.imstargg.storage.db.core.BaseEntity;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "unregistered_battle_event",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_unregistered_battle_event__eventbrawlstarsid",
                        columnNames = "event_brawlstars_id"
                )
        }
)
public class UnregisteredBattleEventCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unregistered_battle_event_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "battle_id", updatable = false, nullable = false)
    private BattleCollectionEntity battle;

    @Embedded
    private BattleCollectionEntityEvent event;

    protected UnregisteredBattleEventCollectionEntity() {
    }

    public UnregisteredBattleEventCollectionEntity(
            BattleCollectionEntity battle
    ) {
        this.battle = battle;
        this.event = battle.getEvent();
    }

    public Long getId() {
        return id;
    }

    public BattleCollectionEntity getBattle() {
        return battle;
    }

    public BattleCollectionEntityEvent getEvent() {
        return event;
    }
}
