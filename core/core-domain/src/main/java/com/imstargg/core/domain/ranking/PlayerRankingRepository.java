package com.imstargg.core.domain.ranking;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.Country;
import com.imstargg.storage.db.core.ranking.BrawlerRankingJpaRepository;
import com.imstargg.storage.db.core.ranking.PlayerRankingJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerRankingRepository {

    private final PlayerRankingJpaRepository playerRankingJpaRepository;
    private final BrawlerRankingJpaRepository brawlerRankingJpaRepository;

    public PlayerRankingRepository(
            PlayerRankingJpaRepository playerRankingJpaRepository,
            BrawlerRankingJpaRepository brawlerRankingJpaRepository
    ) {
        this.playerRankingJpaRepository = playerRankingJpaRepository;
        this.brawlerRankingJpaRepository = brawlerRankingJpaRepository;
    }

    public List<PlayerRanking> findPlayerRanking(Country country) {
        return playerRankingJpaRepository.findAllByCountry(country).stream()
                .map(entity -> new PlayerRanking(
                        new BrawlStarsTag(entity.getPlayer().getBrawlStarsTag()),
                        entity.getPlayer().getName(),
                        entity.getPlayer().getNameColor(),
                        entity.getPlayer().getClubName(),
                        new BrawlStarsId(entity.getPlayer().getIconBrawlStarsId()),
                        entity.getTrophies(),
                        entity.getRank()
                )).toList();
    }

    public List<PlayerRanking> findBrawlerRanking(Country country, BrawlStarsId brawlerId) {
        return brawlerRankingJpaRepository.findAllByCountryAndBrawlerBrawlStarsId(country, brawlerId.value()).stream()
                .map(entity -> new PlayerRanking(
                        new BrawlStarsTag(entity.getPlayer().getBrawlStarsTag()),
                        entity.getPlayer().getName(),
                        entity.getPlayer().getNameColor(),
                        entity.getPlayer().getClubName(),
                        new BrawlStarsId(entity.getPlayer().getIconBrawlStarsId()),
                        entity.getTrophies(),
                        entity.getRank()
                )).toList();
    }
}
