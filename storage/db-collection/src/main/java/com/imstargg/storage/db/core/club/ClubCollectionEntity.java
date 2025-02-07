package com.imstargg.storage.db.core.club;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "club")
public class ClubCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    @Column(name = "brawlstars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Column(name = "name", length = 105, updatable = false, nullable = false)
    private String name;

    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @Column(name = "type", length = 45, nullable = false)
    private String type;

    @Column(name = "badge_brawlstars_id", updatable = false, nullable = false)
    private long badgeBrawlStarsId;

    @Column(name = "required_trophies", nullable = false)
    private int requiredTrophies;

    @Column(name = "trophies", nullable = false)
    private int trophies;

    protected ClubCollectionEntity() {
    }

    public ClubCollectionEntity(
            String brawlStarsTag,
            String name,
            String description,
            String type,
            long badgeBrawlStarsId,
            int requiredTrophies,
            int trophies
    ) {
        this.brawlStarsTag = brawlStarsTag;
        this.name = name;
        this.description = description;
        this.type = type;
        this.badgeBrawlStarsId = badgeBrawlStarsId;
        this.requiredTrophies = requiredTrophies;
        this.trophies = trophies;
    }

    public Long getId() {
        return id;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public long getBadgeBrawlStarsId() {
        return badgeBrawlStarsId;
    }

    public int getRequiredTrophies() {
        return requiredTrophies;
    }

    public int getTrophies() {
        return trophies;
    }
}
