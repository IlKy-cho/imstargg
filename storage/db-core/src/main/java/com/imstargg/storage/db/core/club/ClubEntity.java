package com.imstargg.storage.db.core.club;

import com.imstargg.core.enums.ClubStatus;
import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "club",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_club__brawlstarstag", columnNames = {"brawlstars_tag"})
        },
        indexes = {
                @Index(name = "ix_club__name", columnList = "name")
        }
)
public class ClubEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    @Column(name = "brawlstars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(45)", nullable = false, updatable = false)
    private ClubStatus status;

    @Column(name = "name", length = 105, updatable = false, nullable = false)
    private String name;

    @Column(name = "description", length = 500, nullable = false, updatable = false)
    private String description;

    @Column(name = "type", length = 45, nullable = false, updatable = false)
    private String type;

    @Column(name = "badge_brawlstars_id", updatable = false, nullable = false)
    private long badgeBrawlStarsId;

    @Column(name = "required_trophies", nullable = false, updatable = false)
    private int requiredTrophies;

    @Column(name = "trophies", nullable = false, updatable = false)
    private int trophies;

    protected ClubEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public ClubStatus getStatus() {
        return status;
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
