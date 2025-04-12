package com.imstargg.storage.db.core.statistics;

import com.imstargg.storage.db.core.support.JpaUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class BrawlerPairBattleResultStatisticsCollectionJpaRepositoryCustomImpl
        implements BrawlerPairBattleResultStatisticsCollectionJpaRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public BrawlerPairBattleResultStatisticsCollectionJpaRepositoryCustomImpl(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public void saveAllWithNative(Collection<BrawlerPairBattleResultStatisticsCollectionEntity> entities) {
        List<BrawlerPairBattleResultStatisticsCollectionEntity> entitiesToInsert = entities.stream()
                .filter(entity -> entity.getId() == null)
                .toList();
        List<BrawlerPairBattleResultStatisticsCollectionEntity> entitiesToUpdate = entities.stream()
                .filter(entity -> entity.getId() != null)
                .toList();
        insertEntities(entitiesToInsert);
        updateEntities(entitiesToUpdate);
    }

    private void insertEntities(List<BrawlerPairBattleResultStatisticsCollectionEntity> entitiesToInsert) {
        if (entitiesToInsert.isEmpty()) {
            return;
        }
        jdbcTemplate.batchUpdate("INSERT INTO " + JpaUtils.getTableName(BrawlerPairBattleResultStatisticsCollectionEntity.class) +
                        " (event_brawlstars_id, brawler_brawlstars_id, tier_range, battle_date, " +
                        "pair_brawler_brawlstars_id, victory_count, defeat_count, draw_count, deleted) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        BrawlerPairBattleResultStatisticsCollectionEntity entity = entitiesToInsert.get(i);
                        ps.setLong(1, entity.getEventBrawlStarsId());
                        ps.setLong(2, entity.getBrawlerBrawlStarsId());
                        ps.setString(3, entity.getTierRange());
                        ps.setDate(4, Date.valueOf(entity.getBattleDate()));
                        ps.setLong(5, entity.getPairBrawlerBrawlStarsId());
                        ps.setLong(6, entity.getVictoryCount());
                        ps.setLong(7, entity.getDefeatCount());
                        ps.setLong(8, entity.getDrawCount());
                        ps.setBoolean(9, entity.isDeleted());
                    }

                    @Override
                    public int getBatchSize() {
                        return entitiesToInsert.size();
                    }
                }
        );
    }

    private void updateEntities(List<BrawlerPairBattleResultStatisticsCollectionEntity> entitiesToUpdate) {
        if (entitiesToUpdate.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate("UPDATE " + JpaUtils.getTableName(BrawlerPairBattleResultStatisticsCollectionEntity.class) +
                        " SET victory_count = ?, defeat_count = ?, draw_count = ?, deleted = ? WHERE brawler_pair_battle_result_stats_id = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        BrawlerPairBattleResultStatisticsCollectionEntity entity = entitiesToUpdate.get(i);
                        ps.setLong(1, entity.getVictoryCount());
                        ps.setLong(2, entity.getDefeatCount());
                        ps.setLong(3, entity.getDrawCount());
                        ps.setBoolean(4, entity.isDeleted());
                        ps.setLong(5, entity.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return entitiesToUpdate.size();
                    }
                }
        );
    }
}
