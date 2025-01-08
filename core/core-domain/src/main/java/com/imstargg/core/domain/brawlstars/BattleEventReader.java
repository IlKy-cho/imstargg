package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.enums.Language;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BattleEventReader {

    private final BattleEventRepository battleEventRepository;

    public BattleEventReader(
            BattleEventRepository battleEventRepository
    ) {
        this.battleEventRepository = battleEventRepository;
    }

    public List<BattleEvent> getEvents(Language language, LocalDate date) {
        return battleEventRepository.findAllEvents(language, date);
    }
}
