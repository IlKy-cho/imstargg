package com.imstargg.storage.db.core.ranking;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class RankingEntityPlayer {

    @Column(name = "player_brawlstars_tag", length = 45, nullable = false)
    private String brawlStarsTag;

    @Column(name = "player_name", length = 105, nullable = false)
    private String name;

    @Nullable
    @Column(name = "player_name_color", length = 45)
    private String nameColor;

    @Column(name = "player_icon_brawlstars_id", nullable = false)
    private long iconBrawlStarsId;

    @Column(name = "player_club_name", length = 105, nullable = false)
    private String clubName;

    protected RankingEntityPlayer() {
    }

    public RankingEntityPlayer(
            String brawlStarsTag,
            String name,
            @Nullable String nameColor,
            long iconBrawlStarsId,
            String clubName
    ) {
        this.brawlStarsTag = brawlStarsTag;
        this.name = name;
        this.nameColor = nameColor;
        this.iconBrawlStarsId = iconBrawlStarsId;
        this.clubName = clubName;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getNameColor() {
        return nameColor;
    }

    public long getIconBrawlStarsId() {
        return iconBrawlStarsId;
    }

    public String getClubName() {
        return clubName;
    }
}
