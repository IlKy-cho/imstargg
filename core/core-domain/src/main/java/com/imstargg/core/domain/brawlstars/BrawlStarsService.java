package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
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
        return battleEventReader.getEvents(date);
    }

    public BattleEvent getEvent(BrawlStarsId brawlStarsId) {
        return battleEventReader.getEvent(brawlStarsId);
    }

    public List<RotationBattleEvent> getRotationEvents() {
        return battleEventReader.getRotationEvents();
    }

    public List<BattleEvent> getSoloRankEvents() {
        return battleEventReader.getSoloRankEvents();
    }
}
