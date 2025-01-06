package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.enums.Language;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class BattleEventReader {

    private final BattleEventRepository battleEventRepository;
    private final BattleEventRepositoryWithCache battleEventRepositoryWithCache;

    public BattleEventReader(
            BattleEventRepository battleEventRepository,
            BattleEventRepositoryWithCache battleEventRepositoryWithCache
    ) {
        this.battleEventRepository = battleEventRepository;
        this.battleEventRepositoryWithCache = battleEventRepositoryWithCache;
    }

    public List<BattleEvent> getEvents(Language language, LocalDate date) {
        return battleEventRepository.findAllEventIds(date).stream()
                .map(id -> battleEventRepositoryWithCache.find(id, language))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
