package com.imstargg.storage.db.core.collection;

import com.imstargg.storage.db.core.BattleCollectionEntityEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "unregistered_battle_event")
public class UnRegisteredBattleEventCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unregistered_battle_event_id", nullable = false)
    private Long id;

    @Embedded
    private BattleCollectionEntityEvent event;

    protected UnRegisteredBattleEventCollectionEntity() {
    }

    public UnRegisteredBattleEventCollectionEntity(BattleCollectionEntityEvent event) {
        this.event = event;
    }

    public Long getId() {
        return id;
    }

    public BattleCollectionEntityEvent getEvent() {
        return event;
    }
}
