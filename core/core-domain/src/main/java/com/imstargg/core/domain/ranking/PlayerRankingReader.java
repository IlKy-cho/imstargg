package com.imstargg.core.domain.ranking;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.Country;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerRankingReader {

    private final PlayerRankingRepository playerRankingRepository;

    public PlayerRankingReader(PlayerRankingRepository playerRankingRepository) {
        this.playerRankingRepository = playerRankingRepository;
    }

    public List<PlayerRanking> getPlayerRanking(Country country) {
        return playerRankingRepository.findPlayerRanking(country);
    }

    public List<PlayerRanking> getBrawlerRanking(Country country, BrawlStarsId brawlerId) {
        return playerRankingRepository.findBrawlerRanking(country, brawlerId);
    }
}
