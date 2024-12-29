package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "battle_map")
public class BattleMapCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_map_id")
    private Long id;

    @Column(name = "code", length = 45, updatable = false, nullable = false)
    private String code;

    @Column(name = "name_message_code", length = 105, updatable = false, nullable = false)
    private String nameMessageCode;

    public BattleMapCollectionEntity() {
        this.code = UUID.randomUUID().toString().substring(0, 8);
        this.nameMessageCode = "battle.map." + code + ".name";
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
