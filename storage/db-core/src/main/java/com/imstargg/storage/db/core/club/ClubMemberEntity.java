package com.imstargg.storage.db.core.club;

import com.imstargg.storage.db.core.BaseEntity;
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
        name = "club_member",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_clubmember__clubid_brawlstarstag", columnNames = {"club_id", "brawlstars_tag"})
        }
)
public class ClubMemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_member_id")
    private Long id;

    @Column(name = "club_id", updatable = false, nullable = false)
    private long clubId;

    @Column(name = "brawlstars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Column(name = "name", length = 45, updatable = false, nullable = false)
    private String name;

    @Nullable
    @Column(name = "name_color", length = 45, updatable = false)
    private String nameColor;

    @Column(name = "role", length = 25, nullable = false, updatable = false)
    private String role;

    @Column(name = "trophies", nullable = false, updatable = false)
    private int trophies;

    @Column(name = "icon_brawlstars_id", nullable = false, updatable = false)
    private long iconBrawlStarsId;

    protected ClubMemberEntity() {}

    public Long getId() {
        return id;
    }

    public long getClubId() {
        return clubId;
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

    public String getRole() {
        return role;
    }

    public int getTrophies() {
        return trophies;
    }

    public long getIconBrawlStarsId() {
        return iconBrawlStarsId;
    }
}
