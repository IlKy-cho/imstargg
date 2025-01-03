package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.enums.Language;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrawlStarsService {

    private final BrawlerReader brawlerReader;
    private final BattleEventReader battleEventReader;

    public BrawlStarsService(BrawlerReader brawlerReader, BattleEventReader battleEventReader) {
        this.brawlerReader = brawlerReader;
        this.battleEventReader = battleEventReader;
    }

    public List<Brawler> getAllBrawlers() {
        return brawlerReader.getAll(Language.KOREAN);
    }

    public List<BattleEvent> getSeasonEvents() {
        return battleEventReader.getSeasonEvents(Language.KOREAN);
    }
}
