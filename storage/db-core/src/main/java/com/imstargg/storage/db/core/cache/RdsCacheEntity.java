package com.imstargg.storage.db.core.cache;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "rds_cache",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_rds_cache__key", columnNames = {"cache_key"})
        }
)
public class RdsCacheEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rds_cache_id")
    private Long id;

    @Column(name = "cache_key", nullable = false, updatable = false)
    private String key;

    @Column(name = "cache_value", nullable = false, updatable = false)
    private String value;

    protected RdsCacheEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
