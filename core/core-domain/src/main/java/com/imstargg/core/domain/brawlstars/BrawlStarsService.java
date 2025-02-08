package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.Language;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        return brawlerReader.getAll();
    }

    public Brawler getBrawler(BrawlStarsId brawlStarsId) {
        return brawlerReader.get(brawlStarsId);
    }

    public List<BattleEvent> getEvents(LocalDate date) {
        return battleEventReader.getEvents(Language.KOREAN, date);
    }

    public BattleEvent getEvent(BrawlStarsId brawlStarsId) {
        return battleEventReader.getEvent(Language.KOREAN, brawlStarsId);
    }
}
