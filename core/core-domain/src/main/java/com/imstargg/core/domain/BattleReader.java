package com.imstargg.core.domain;

import org.springframework.stereotype.Component;

@Component
public class BattleReader {

    private final BattleRepository battleRepository;

    public BattleReader(BattleRepository battleRepository) {
        this.battleRepository = battleRepository;
    }

    public Slice<PlayerBattle> getList(Player player, int page) {
        return battleRepository.find(player, page + 1);
    }
}
