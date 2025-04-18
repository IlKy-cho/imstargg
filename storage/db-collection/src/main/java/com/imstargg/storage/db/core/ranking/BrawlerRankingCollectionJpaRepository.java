package com.imstargg.storage.db.core.ranking;

import com.imstargg.core.enums.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrawlerRankingCollectionJpaRepository extends JpaRepository<BrawlerRankingCollectionEntity, Long> {

    List<BrawlerRankingCollectionEntity> findAllByCountryAndBrawlerBrawlStarsId(
            Country country, long brawlerBrawlStarsId);
}
