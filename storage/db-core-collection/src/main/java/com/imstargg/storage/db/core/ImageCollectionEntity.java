package com.imstargg.storage.db.core;

import com.imstargg.core.enums.ImageBucket;
import com.imstargg.core.enums.ImageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")
public class ImageCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "images_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "bucket", length = 25, updatable = false, nullable = false)
    private ImageBucket bucket;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 25, updatable = false, nullable = false)
    private ImageType type;

    @Column(name = "code", length = 65, updatable = false, nullable = false)
    private String code;

    @Column(name = "stored_name", updatable = false, nullable = false)
    private String storedName;

    @Column(name = "url", length = 500, nullable = false)
    private String url;

    protected ImageCollectionEntity() {
    }

    public ImageCollectionEntity(
            ImageType type,
            String code,
            String storedName,
            String url
    ) {
        this.bucket = type.getBucket();
        this.type = type;
        this.code = code;
        this.storedName = storedName;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public ImageBucket getBucket() {
        return bucket;
    }

    public ImageType getType() {
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
