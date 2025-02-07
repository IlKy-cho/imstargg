package com.imstargg.storage.db.core.club;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
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
@Table(name = "club_member")
public class ClubMemberCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "club_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ClubCollectionEntity club;

    @Column(name = "brawlstars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @Column(name = "name_color", length = 45)
    private String nameColor;

    @Column(name = "role", length = 25, nullable = false)
    private String role;

    @Column(name = "trophies", nullable = false)
    private int trophies;

    @Column(name = "icon_brawlstars_id", nullable = false)
    private long iconBrawlStarsId;

    protected ClubMemberCollectionEntity() {}

    public ClubMemberCollectionEntity(
            ClubCollectionEntity club,
            String brawlStarsTag,
            String name,
            String nameColor,
            String role,
            int trophies,
            long iconBrawlStarsId
    ) {
        this.club = club;
        this.brawlStarsTag = brawlStarsTag;
        this.name = name;
        this.nameColor = nameColor;
        this.role = role;
        this.trophies = trophies;
        this.iconBrawlStarsId = iconBrawlStarsId;
    }

    public Long getId() {
        return id;
    }

    public ClubCollectionEntity getClub() {
        return club;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public String getName() {
        return name;
    }

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
