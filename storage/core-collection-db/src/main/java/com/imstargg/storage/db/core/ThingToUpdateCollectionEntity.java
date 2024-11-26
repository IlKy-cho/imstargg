package com.imstargg.storage.db.core;

import com.imstargg.core.enums.UpdateType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "thing_to_update")
public class ThingToUpdateCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thing_to_update_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "varchar(25)", updatable = false, nullable = false)
    private UpdateType type;

    @Column(name = "brawlstars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "data", length = 4000, nullable = false)
    private String data;

    protected ThingToUpdateCollectionEntity() {
    }

    public ThingToUpdateCollectionEntity(UpdateType type, long brawlStarsId, String data) {
        this.type = type;
        this.brawlStarsId = brawlStarsId;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public UpdateType getType() {
        return type;
    }

    public long getBrawlStarsId() {
        return brawlStarsId;
    }

    public String getData() {
        return data;
    }
}
