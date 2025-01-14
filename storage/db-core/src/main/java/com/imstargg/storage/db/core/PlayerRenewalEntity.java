package com.imstargg.storage.db.core;

import com.imstargg.core.enums.PlayerRenewalStatus;
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
import jakarta.persistence.Version;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "player_renewal",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_brawlstarstag", columnNames = "brawlstars_tag")
        },
        indexes = {
                @Index(name = "ix_status", columnList = "status"),
        }
)
public class PlayerRenewalEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_renewal_id")
    private Long id;

    @Column(name = "brawlstars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(25)", nullable = false)
    private PlayerRenewalStatus status;

    @Column(name = "requested_at", nullable = false)
    private OffsetDateTime requestedAt;

    @Version
    private int version;

    protected PlayerRenewalEntity() {
    }

    public PlayerRenewalEntity(String brawlStarsTag, OffsetDateTime requestedAt) {
        this.brawlStarsTag = brawlStarsTag;
        this.status = PlayerRenewalStatus.PENDING;
        this.requestedAt = requestedAt;
    }

    public void pending(OffsetDateTime requestedAt) {
        this.status = PlayerRenewalStatus.PENDING;
        this.requestedAt = requestedAt;
    }

    public Long getId() {
        return id;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public PlayerRenewalStatus getStatus() {
        return status;
    }

    public OffsetDateTime getRequestedAt() {
        return requestedAt;
    }
}
