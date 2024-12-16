package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.core.enums.BrawlStarsImageType;
import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "brawlstars_image")
public class BrawlStarsImageCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brawlstars_image_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 25, updatable = false, nullable = false)
    private BrawlStarsImageType type;

    @Column(name = "code", length = 65, updatable = false, nullable = false)
    private String code;

    @Column(name = "stored_name", updatable = false, nullable = false)
    private String storedName;

    @Column(name = "url", length = 500, nullable = false)
    private String url;

    protected BrawlStarsImageCollectionEntity() {
    }

    public BrawlStarsImageCollectionEntity(
            BrawlStarsImageType type,
            String code,
            String storedName,
            String url
    ) {
        this.type = type;
        this.code = code;
        this.storedName = storedName;
        this.url = url;
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

    public String getUrl() {
        return url;
    }
}
