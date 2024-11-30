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

import java.time.Clock;
import java.time.LocalDateTime;

@Entity
@Table(name = "unknown_player")
public class UnknownPlayerCollectionEntity extends BaseEntity {

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

    @Column(name = "update_available_at", updatable = false, nullable = false)
    private LocalDateTime updateAvailableAt;

    protected UnknownPlayerCollectionEntity() {
    }

    private UnknownPlayerCollectionEntity(
            String brawlStarsTag,
            UnknownPlayerStatus status,
            int notFoundCount,
            LocalDateTime updateAvailableAt
    ) {
        this.brawlStarsTag = brawlStarsTag;
        this.status = status;
        this.notFoundCount = notFoundCount;
        this.updateAvailableAt = updateAvailableAt;
    }

    public static UnknownPlayerCollectionEntity adminNew(
            String brawlStarsTag,
            Clock clock
    ) {
        return new UnknownPlayerCollectionEntity(
                brawlStarsTag,
                UnknownPlayerStatus.ADMIN_NEW,
                0,
                LocalDateTime.now(clock)
        );
    }

    public static UnknownPlayerCollectionEntity updateNew(
            String brawlStarsTag,
            Clock clock
    ) {
        return new UnknownPlayerCollectionEntity(
                brawlStarsTag,
                UnknownPlayerStatus.UPDATE_NEW,
                0,
                LocalDateTime.now(clock)
        );
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

    public LocalDateTime getUpdateAvailableAt() {
        return updateAvailableAt;
    }
}
