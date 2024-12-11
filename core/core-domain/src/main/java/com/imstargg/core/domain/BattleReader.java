package com.imstargg.core.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BattleReader {

    private final BattleRepository battleRepository;

    public BattleReader(BattleRepository battleRepository) {
        this.battleRepository = battleRepository;
    }

    public List<PlayerBattle> getList(Player player, int page) {
        return battleRepository.find(player, page + 1);
    }
}
