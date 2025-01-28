package com.imstargg.storage.db.core.ranking;

import com.imstargg.core.enums.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrawlerRankingJpaRepository extends JpaRepository<BrawlerRankingEntity, Long> {

    List<BrawlerRankingEntity> findAllByCountryAndBrawlerBrawlStarsId(Country country, long brawlerBrawlStarsId);
}
