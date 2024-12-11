package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "battle_map",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_battle_map__code",
                        columnNames = "code"
                )
        }
)
public class BattleMapEntity extends BaseEntity {

    @Id
    @Column(name = "battle_map_id")
    private Long id;

    @Column(name = "code", length = 45, updatable = false, nullable = false)
    private String code;

    @Column(name = "name_message_code", length = 105, updatable = false, nullable = false)
    private String nameMessageCode;

    protected BattleMapEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getNameMessageCode() {
        return nameMessageCode;
    }
}
