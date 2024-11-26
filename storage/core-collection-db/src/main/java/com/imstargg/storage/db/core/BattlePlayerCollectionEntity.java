package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "battle_player")
public class BattlePlayerCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_player_id")
    private Long id;

    @Column(name = "battle_id", updatable = false, nullable = false)
    private long battleId;

    @Column(name = "brawlstars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Column(name = "brawler_brawlstars_id", updatable = false, nullable = false)
    private long brawlerBrawlStarsId;

    @Column(name = "power", updatable = false, nullable = false)
    private int power;

    @Nullable
    @Column(name = "trophies", updatable = false)
    private Integer trophies;

    @Column(name = "team_idx", updatable = false, nullable = false)
    private int teamIdx;

    @Column(name = "idx", updatable = false, nullable = false)
    private int idx;

    protected BattlePlayerCollectionEntity() {
    }

    public BattlePlayerCollectionEntity(
            long battleId,
            String brawlStarsTag,
            long brawlerId,
            int power,
            @Nullable Integer trophies,
            int teamIdx,
            int idx
    ) {
        this.battleId = battleId;
        this.brawlStarsTag = brawlStarsTag;
        this.brawlerBrawlStarsId = brawlerId;
        this.power = power;
        this.trophies = trophies;
        this.teamIdx = teamIdx;
        this.idx = idx;
    }

    public Long getId() {
        return id;
    }

    public long getBattleId() {
        return battleId;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public long getBrawlerBrawlStarsId() {
        return brawlerBrawlStarsId;
    }

    public int getPower() {
        return power;
    }

    @Nullable
    public Integer getTrophies() {
        return trophies;
    }

    public int getTeamIdx() {
        return teamIdx;
    }

    public int getIdx() {
        return idx;
    }
}
