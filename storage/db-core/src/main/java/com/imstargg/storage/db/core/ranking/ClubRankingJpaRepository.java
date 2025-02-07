package com.imstargg.storage.db.core.ranking;

import com.imstargg.core.enums.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRankingJpaRepository extends JpaRepository<ClubRankingEntity, Long> {

    List<ClubRankingEntity> findAllByCountry(Country country);
}
