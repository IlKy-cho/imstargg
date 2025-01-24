package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "brawlstars_image",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_brawlstars_image__code", columnNames = {"code"})
        }
)
public class BrawlStarsImageEntity extends BaseEntity {

    @Id
    @Column(name = "brawlstars_image_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 25, updatable = false, nullable = false)
    private BrawlStarsImageType type;

    @Column(name = "code", length = 65, updatable = false, nullable = false)
    private String code;

    @Column(name = "stored_name", updatable = false, nullable = false)
    private String storedName;

    protected BrawlStarsImageEntity() {
    }

    public Long getId() {
        return id;
    }

    public BrawlStarsImageType getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getStoredName() {
        return storedName;
    }
}
