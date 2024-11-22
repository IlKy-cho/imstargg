package com.imstargg.storage.db.core;

import com.imstargg.core.enums.GadgetTier;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "gadget",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_gadget__brawlstarsid",
                        columnNames = "brawl_stars_id"
                )
        }
)
public class GadgetEntity extends BaseEntity {

    @Id
    @Column(name = "gadget_id")
    private Long id;

    @Column(name = "brawl_stars_id", updatable = false, nullable = false)
    private long brawlStarsId;

    @Column(name = "name", length = 105, updatable = false, nullable = false)
    private String name;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "tier", columnDefinition = "varchar(45)")
    private GadgetTier tier;

    protected GadgetEntity() {
    }

    public Long getId() {
        return id;
    }

    public long getBrawlStarsId() {
        return brawlStarsId;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public GadgetTier getTier() {
        return tier;
    }
}
