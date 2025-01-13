package com.imstargg.storage.db.core;

import com.imstargg.core.enums.PlayerRenewalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "player_renewal")
public class PlayerRenewalCollectionEntity extends BaseEntity {

    @Id
    @Column(name = "player_renewal_id")
    private Long id;

    @Column(name = "brawlstars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(25)", nullable = false)
    private PlayerRenewalStatus status;

    @Version
    private int version;

    protected PlayerRenewalCollectionEntity() {
    }

    public void executing() {
        this.status = PlayerRenewalStatus.EXECUTING;
    }

    public void complete() {
        this.status = PlayerRenewalStatus.COMPLETE;
    }

    public void failed() {
        this.status = PlayerRenewalStatus.FAILED;
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
}
