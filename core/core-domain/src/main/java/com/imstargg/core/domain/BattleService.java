package com.imstargg.core.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BattleService {

    private final PlayerReader playerReader;
    private final BattleReader battleReader;

    public BattleService(PlayerReader playerReader, BattleReader battleReader) {
        this.playerReader = playerReader;
        this.battleReader = battleReader;
    }

    public List<Battle> getPlayerBattles(BrawlStarsTag tag, int page) {
        Player player = playerReader.get(tag);
        return battleReader.getList(player, page);
    }
}
