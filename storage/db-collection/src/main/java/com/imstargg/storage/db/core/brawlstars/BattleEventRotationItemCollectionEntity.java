package com.imstargg.storage.db.core.brawlstars;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class BattleEventRotationItemCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_event_rotation_item_id")
    private Long id;

    @Column(name = "battle_event_rotation_id", updatable = false, nullable = false)
    private long battleEventRotationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "event_brawlstars_id", updatable = false, nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private BattleEventCollectionEntity event;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "modifiers",columnDefinition = "json", updatable = false, nullable = false)
    private List<String> modifiers;

    @Column(name = "slot_id", updatable = false, nullable = false)
    private long slotId;

    @Column(name = "start_time", updatable = false, nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "end_time", updatable = false, nullable = false)
    private OffsetDateTime endTime;

    protected BattleEventRotationItemCollectionEntity() {
    }

    public BattleEventRotationItemCollectionEntity(
            long battleEventRotationId,
            BattleEventCollectionEntity event,
            List<String> modifiers,
            long slotId,
            OffsetDateTime startTime,
            OffsetDateTime endTime
    ) {
        this.battleEventRotationId = battleEventRotationId;
        this.event = event;
        this.modifiers = modifiers;
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public long getBattleEventRotationId() {
        return battleEventRotationId;
    }

    public BattleEventCollectionEntity getEvent() {
        return event;
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
