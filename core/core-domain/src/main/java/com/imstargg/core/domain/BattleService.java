package com.imstargg.core.domain;

import com.imstargg.core.enums.Language;
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
        return battleReader.getList(player, page, Language.KOREAN);
    }
}
