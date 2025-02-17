package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.Language;
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

    public List<BattleEvent> getEvents(Language language, LocalDate date) {
        return battleEventRepository.findAllEvents(language, date);
    }

    public BattleEvent getEvent(Language language, BrawlStarsId brawlStarsId) {
        return battleEventRepository.find(brawlStarsId, language)
                .orElseThrow(() -> new CoreException(CoreErrorType.BATTLE_EVENT_NOT_FOUND,
                        "brawlStarsId: " + brawlStarsId));
    }

    public List<RotationBattleEvent> getRotationEvents(Language language) {
        return battleEventRepository.findAllRotation(language);
    }

    public List<BattleEvent> getSoloRankEvents(Language language) {
        return battleEventRepository.findAllSoloRank(language);
    }
}
