package com.imstargg.storage.db.core;

import com.imstargg.core.enums.UnknownPlayerStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(45)", nullable = false)
    private UnknownPlayerStatus status;

    @Column(name = "not_found_count", nullable = false)
    private int notFoundCount;

    protected UnknownPlayerEntity() {
    }

    public UnknownPlayerEntity(String brawlStarsTag, UnknownPlayerStatus status) {
        this.brawlStarsTag = brawlStarsTag;
        this.status = status;
        this.notFoundCount = 0;
    }

    public static UnknownPlayerEntity newSearchNew(String brawlStarsTag) {
        return new UnknownPlayerEntity(brawlStarsTag, UnknownPlayerStatus.SEARCH_NEW);
    }

    public void searchNew() {
        this.status = UnknownPlayerStatus.SEARCH_NEW;
    }

    public Long getId() {
        return id;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public UnknownPlayerStatus getStatus() {
        return status;
    }

    public int getNotFoundCount() {
        return notFoundCount;
    }

}
