package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embedded;
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
@Table(name = "battle_player")
public class BattlePlayerCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_player_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "battle_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private BattleCollectionEntity battle;

    @Column(name = "brawlstars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Column(name = "name", length = 105, nullable = false)
    private String name;

    @Column(name = "team_idx", updatable = false, nullable = false)
    private int teamIdx;

    @Column(name = "player_idx", updatable = false, nullable = false)
    private int playerIdx;

    @Embedded
    private BattlePlayerCollectionEntityBrawler brawler;

    protected BattlePlayerCollectionEntity() {
    }

    BattlePlayerCollectionEntity(
            BattleCollectionEntity battle,
            String brawlStarsTag,
            String name,
            int teamIdx,
            int playerIdx,
            BattlePlayerCollectionEntityBrawler brawler
    ) {
        this.battle = battle;
        this.brawlStarsTag = brawlStarsTag;
        this.name = name;
        this.teamIdx = teamIdx;
        this.playerIdx = playerIdx;
        this.brawler = brawler;
    }

    public Long getId() {
        return id;
    }

    public BattleCollectionEntity getBattle() {
        return battle;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public String getName() {
        return name;
    }

    public int getTeamIdx() {
        return teamIdx;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public BattlePlayerCollectionEntityBrawler getBrawler() {
        return brawler;
    }
}
