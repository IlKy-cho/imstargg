package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "battle",
        indexes = {
                @Index(name = "ix_battle__eventbrawlstarsid", columnList = "event_brawlstars_id"),
                @Index(name = "ix_battle__createdat", columnList = "created_at desc"),
        }
)
public class BattleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_id", nullable = false)
    private Long id;

    @Column(name = "battle_key", updatable = false, nullable = false)
    private String battleKey;

    @Column(name = "battle_time", updatable = false, nullable = false)
    private LocalDateTime battleTime;

    @Column(name = "event_brawlstars_id", updatable = false, nullable = false)
    private long eventBrawlStarsId;

    @Column(name = "mode", length = 105, updatable = false, nullable = false)
    private String mode;

    @Column(name = "type", length = 105, updatable = false, nullable = false)
    private String type;

    @Column(name = "result", length = 25, updatable = false, nullable = false)
    private String result;

    @Column(name = "duration", updatable = false, nullable = false)
    private int duration;

    @Nullable
    @Column(name = "star_player_brawlstars_tag", length = 45, updatable = false)
    private String starPlayerBrawlStarsTag;

    @Embedded
    private BattleEntityPlayerEmbeddable player;

    protected BattleEntity() {
    }

}
