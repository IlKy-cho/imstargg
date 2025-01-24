package com.imstargg.core.domain;

import com.imstargg.core.enums.Language;
import org.springframework.stereotype.Component;

@Component
public class BattleReader {

    private final BattleRepository battleRepository;

    public BattleReader(BattleRepository battleRepository) {
        this.battleRepository = battleRepository;
    }

    public Slice<PlayerBattle> getList(Player player, int page, Language language) {
        return battleRepository.find(player, page, language);
    }
}
