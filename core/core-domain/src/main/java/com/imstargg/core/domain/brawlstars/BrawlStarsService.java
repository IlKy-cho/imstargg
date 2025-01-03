package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.enums.Language;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrawlStarsService {

    private final BrawlerReader brawlerReader;

    public BrawlStarsService(BrawlerReader brawlerReader) {
        this.brawlerReader = brawlerReader;
    }

    public List<Brawler> getAllBrawlers() {
        return brawlerReader.getAll(Language.KOREAN);
    }

    public List<BattleEvent> getSeasonEvents() {

    }
}
