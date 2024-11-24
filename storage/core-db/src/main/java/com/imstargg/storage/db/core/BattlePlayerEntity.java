package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "battle_player",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_battleplayer__battleid_brawlstarstag", columnNames = {"battle_id", "brawlstars_tag"})
        }
)
public class BattlePlayerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_player_id")
    private Long id;

    @Column(name = "battle_id", updatable = false, nullable = false)
    private long battleId;

    @Column(name = "brawlstars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Column(name = "brawler_id", updatable = false, nullable = false)
    private long brawlerId;

    @Column(name = "power", updatable = false, nullable = false)
    private int power;

    @Nullable
    @Column(name = "trophies", updatable = false)
    private Integer trophies;

    @Column(name = "team_idx", updatable = false, nullable = false)
    private int teamIdx;

    @Column(name = "idx", updatable = false, nullable = false)
    private int idx;

    protected BattlePlayerEntity() {
    }

}
