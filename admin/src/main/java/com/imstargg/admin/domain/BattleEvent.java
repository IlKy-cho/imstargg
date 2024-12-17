package com.imstargg.admin.domain;

import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionEntity;

public record BattleEvent(
        BattleEventCollectionEntity entity,
        BattleEventMap map
) {
}
