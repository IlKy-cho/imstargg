package com.imstargg.storage.db.core;

import com.imstargg.core.enums.ImageBucket;
import com.imstargg.core.enums.ImageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "images",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_images__code", columnNames = {"code"})
        }
)
public class ImageEntity extends BaseEntity {

    @Id
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

    protected ImageEntity() {
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
