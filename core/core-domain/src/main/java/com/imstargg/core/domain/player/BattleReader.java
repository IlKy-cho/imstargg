package com.imstargg.core.domain.player;

import com.imstargg.core.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class BattleReader {

    private static final int PAGE_SIZE = 25;

    private final BattleRepository battleRepository;

    public BattleReader(BattleRepository battleRepository) {
        this.battleRepository = battleRepository;
    }

    public Slice<PlayerBattle> getList(Player player, int page) {
        return battleRepository.find(player, page, PAGE_SIZE);
    }
}
