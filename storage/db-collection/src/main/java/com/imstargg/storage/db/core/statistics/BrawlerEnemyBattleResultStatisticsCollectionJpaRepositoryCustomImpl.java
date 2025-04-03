package com.imstargg.storage.db.core.statistics;

import com.imstargg.storage.db.core.support.JpaUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.List;

public class BrawlerEnemyBattleResultStatisticsCollectionJpaRepositoryCustomImpl
        implements BrawlerEnemyBattleResultStatisticsCollectionJpaRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public BrawlerEnemyBattleResultStatisticsCollectionJpaRepositoryCustomImpl(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public void saveAllWithNative(Clock clock, List<BrawlerEnemyBattleResultStatisticsCollectionEntity> entities) {
        List<BrawlerEnemyBattleResultStatisticsCollectionEntity> entitiesToInsert = entities.stream()
                .filter(entity -> entity.getId() == null)
                .toList();
        List<BrawlerEnemyBattleResultStatisticsCollectionEntity> entitiesToUpdate = entities.stream()
                .filter(entity -> entity.getId() != null)
                .toList();
        Instant now = clock.instant();
        insertEntities(entitiesToInsert, now);
        updateEntities(entitiesToUpdate, now);
    }

    private void insertEntities(List<BrawlerEnemyBattleResultStatisticsCollectionEntity> entitiesToInsert, Instant now) {
        if (entitiesToInsert.isEmpty()) {
            return;
        }
        jdbcTemplate.batchUpdate("INSERT INTO " + JpaUtils.getTableName(BrawlerEnemyBattleResultStatisticsCollectionEntity.class) +
                        " (event_brawlstars_id, brawler_brawlstars_id, tier_range, battle_date, " +
                        "enemy_brawler_brawlstars_id, victory_count, defeat_count, draw_count, deleted) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        BrawlerEnemyBattleResultStatisticsCollectionEntity entity = entitiesToInsert.get(i);
                        ps.setLong(1, entity.getEventBrawlStarsId());
                        ps.setLong(2, entity.getBrawlerBrawlStarsId());
                        ps.setString(3, entity.getTierRange());
                        ps.setDate(4, Date.valueOf(entity.getBattleDate()));
                        ps.setLong(5, entity.getEnemyBrawlerBrawlStarsId());
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

    private void updateEntities(List<BrawlerEnemyBattleResultStatisticsCollectionEntity> entitiesToUpdate, Instant now) {
        if (entitiesToUpdate.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate("UPDATE " + JpaUtils.getTableName(BrawlerEnemyBattleResultStatisticsCollectionEntity.class) +
                        " SET victory_count = ?, defeat_count = ?, draw_count = ?, deleted = ? WHERE brawler_enemy_battle_result_stats_id = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        BrawlerEnemyBattleResultStatisticsCollectionEntity entity = entitiesToUpdate.get(i);
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
