package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "club",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_club__brawlstarstag", columnNames = {"brawl_stars_tag"})
        },
        indexes = {
                @Index(name = "ix_club__name", columnList = "name")
        }
)
public class ClubEntity extends BaseEntity {

    @Id
    @Column(name = "club_id")
    private Long id;

    @Column(name = "brawl_stars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Column(name = "name", length = 105, updatable = false, nullable = false)
    private String name;

    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @Column(name = "type", length = 45, nullable = false)
    private String type;

    @Column(name = "badge_id", updatable = false, nullable = false)
    private long badgeId;

    @Column(name = "required_trophies", nullable = false)
    private int requiredTrophies;

    @Column(name = "trophies", nullable = false)
    private int trophies;

    protected ClubEntity() {
        this.id = id;
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

    public long getBadgeId() {
        return badgeId;
    }

    public int getRequiredTrophies() {
        return requiredTrophies;
    }

    public int getTrophies() {
        return trophies;
    }
}
