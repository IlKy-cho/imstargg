package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity
@Table(
        name = "unknown_player",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_unknownplayer__brawlstarstag",
                        columnNames = "brawlstars_tag"
                )
        }
)
public class UnknownPlayerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unknown_player_id")
    private Long id;

    @Column(name = "brawlstars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Column(name = "not_found_count", nullable = false)
    private int notFoundCount;

    @Version
    private int version;

    protected UnknownPlayerEntity() {
    }

    public UnknownPlayerEntity(String brawlStarsTag) {
        this.brawlStarsTag = brawlStarsTag;
        this.notFoundCount = 0;
    }

    public Long getId() {
        return id;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public int getNotFoundCount() {
        return notFoundCount;
    }

}
