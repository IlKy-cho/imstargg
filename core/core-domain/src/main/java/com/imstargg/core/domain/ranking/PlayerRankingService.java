package com.imstargg.core.domain.ranking;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.Country;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerRankingService {

    private final PlayerRankingReader playerRankingReader;

    public PlayerRankingService(PlayerRankingReader playerRankingReader) {
        this.playerRankingReader = playerRankingReader;
    }

    public List<PlayerRanking> getPlayerRanking(Country country) {
        return playerRankingReader.getPlayerRanking(country);
    }

    public List<PlayerRanking> getBrawlerRanking(Country country, BrawlStarsId brawlerId) {
        return playerRankingReader.getBrawlerRanking(country, brawlerId);
    }
}
