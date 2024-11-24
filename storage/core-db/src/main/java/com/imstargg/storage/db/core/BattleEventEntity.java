package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "battle_event",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_battleevent__brawlstarsid",
                        columnNames = "brawlstars_id"
                )
        }
)
public class BattleEventEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_event_id")
    private Long id;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "mode", length = 105, updatable = false, nullable = false)
    private String mode;

    @Column(name = "map", length = 105, updatable = false, nullable = false)
    private String map;

    protected BattleEventEntity() {
    }

}
