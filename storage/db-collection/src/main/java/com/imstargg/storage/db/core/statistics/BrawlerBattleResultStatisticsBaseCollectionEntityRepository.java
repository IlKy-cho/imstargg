package com.imstargg.storage.db.core.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BrawlerBattleResultStatisticsBaseCollectionEntityRepository<T extends BrawlerBattleResultStatisticsBaseCollectionEntity, ID> extends JpaRepository<T, ID> {
}