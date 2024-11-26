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
public class ThingToUpdateCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thing_to_update_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "varchar(25)", updatable = false, nullable = false)
    private UpdateType type;

    @Column(name = "data", length = 4000, nullable = false)
    private String data;

    protected ThingToUpdateCollectionEntity() {
    }

    public ThingToUpdateCollectionEntity(UpdateType type, String data) {
        this.type = type;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public UpdateType getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}
