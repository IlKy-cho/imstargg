package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BattleEventReader {

    private final BattleEventRepositoryWithCache battleEventRepository;

    public BattleEventReader(
            BattleEventRepositoryWithCache battleEventRepository
    ) {
        this.battleEventRepository = battleEventRepository;
    }

    public List<BattleEvent> getEvents(LocalDate date) {
        return battleEventRepository.findAllEvents(date);
    }

    public BattleEvent getEvent(BrawlStarsId brawlStarsId) {
        return battleEventRepository.find(brawlStarsId)
                .orElseThrow(() -> new CoreException(CoreErrorType.BATTLE_EVENT_NOT_FOUND,
                        "brawlStarsId: " + brawlStarsId));
    }

    public List<RotationBattleEvent> getRotationEvents() {
        return battleEventRepository.findAllRotation();
    }

    public List<BattleEvent> getSoloRankEvents() {
        return battleEventRepository.findAllSoloRank();
    }
}
