package com.imstargg.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "illegal_player",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_illegalplayer__brawlstarstag",
                        columnNames = "brawlstars_tag"
                )
        }
)
public class IllegalPlayerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "illegal_player_id")
    private Long id;

    @Column(name = "brawlstars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Column(name = "count", nullable = false)
    private int count;

    @Column(name = "available_at", updatable = false, nullable = false)
    private LocalDateTime availableAt;

    protected IllegalPlayerEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public int getCount() {
        return count;
    }

    public LocalDateTime getAvailableAt() {
        return availableAt;
    }
}
