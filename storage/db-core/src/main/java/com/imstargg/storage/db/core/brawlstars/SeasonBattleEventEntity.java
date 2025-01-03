package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "season_battle_event",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_seasoned_battle_event__battleeventid",
                        columnNames = "battle_event_id"
                )
        }
)
public class SeasonBattleEventEntity extends BaseEntity {

    @Id
    @Column(name = "season_battle_event_id")
    private Long id;

    @Column(name = "battle_event_id", updatable = false, nullable = false)
    private long battleEventId;

    protected SeasonBattleEventEntity() {
    }

    public Long getId() {
        return id;
    }

    public long getBattleEventId() {
        return battleEventId;
    }
}
