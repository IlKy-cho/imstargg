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

import java.time.LocalDateTime;

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

    @Column(name = "update_available_at", updatable = false, nullable = false)
    private LocalDateTime updatedAvailableAt;

    protected UnknownPlayerEntity() {
    }

}
