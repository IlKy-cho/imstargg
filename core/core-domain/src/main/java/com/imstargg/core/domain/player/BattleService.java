package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class BattleService {

    private final PlayerReader playerReader;
    private final BattleReader battleReader;

    public BattleService(PlayerReader playerReader, BattleReader battleReader) {
        this.playerReader = playerReader;
        this.battleReader = battleReader;
    }

    public Slice<PlayerBattle> getPlayerBattles(BrawlStarsTag tag, int page) {
        Player player = playerReader.get(tag);
        return battleReader.getList(player, page);
    }
}
