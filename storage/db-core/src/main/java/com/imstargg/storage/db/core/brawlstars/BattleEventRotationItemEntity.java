package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(
        name = "battle_event_rotation_item",
        indexes = @Index(name = "ix_battle_event_rotation_item__1", columnList = "battle_event_rotation_id")
)
public class BattleEventRotationItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_event_rotation_item_id")
    private Long id;

    @Column(name = "battle_event_rotation_id", updatable = false, nullable = false)
    private long battleEventRotationId;

    @Column(name = "event_brawlstars_id", updatable = false, nullable = false)
    private long eventBrawlStarsId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "modifiers",columnDefinition = "json", updatable = false, nullable = false)
    private List<String> modifiers;

    @Column(name = "slot_id", updatable = false, nullable = false)
    private int slotId;

    @Column(name = "start_time", updatable = false, nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "end_time", updatable = false, nullable = false)
    private OffsetDateTime endTime;

    protected BattleEventRotationItemEntity() {
    }

    public Long getId() {
        return id;
    }

    public long getBattleEventRotationId() {
        return battleEventRotationId;
    }

    public long getEventBrawlStarsId() {
        return eventBrawlStarsId;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public long getSlotId() {
        return slotId;
    }

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public OffsetDateTime getEndTime() {
        return endTime;
    }
}
