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

public class BrawlerBattleResultStatisticsCollectionJpaRepositoryCustomImpl
        implements BrawlerBattleResultStatisticsCollectionJpaRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public BrawlerBattleResultStatisticsCollectionJpaRepositoryCustomImpl(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public void saveAllWithNative(Clock clock, List<BrawlerBattleResultStatisticsCollectionEntity> entities) {
        List<BrawlerBattleResultStatisticsCollectionEntity> entitiesToInsert = entities.stream()
                .filter(entity -> entity.getId() == null)
                .toList();
        List<BrawlerBattleResultStatisticsCollectionEntity> entitiesToUpdate = entities.stream()
                .filter(entity -> entity.getId() != null)
                .toList();
        Instant now = clock.instant();
        insertEntities(entitiesToInsert, now);
        updateEntities(entitiesToUpdate, now);
    }

    private void insertEntities(List<BrawlerBattleResultStatisticsCollectionEntity> entitiesToInsert, Instant now) {
        if (entitiesToInsert.isEmpty()) {
            return;
        }
        jdbcTemplate.batchUpdate("INSERT INTO " + JpaUtils.getTableName(BrawlerBattleResultStatisticsCollectionEntity.class) +
                        " (event_brawlstars_id, brawler_brawlstars_id, tier_range, battle_date, " +
                        "victory_count, defeat_count, draw_count, star_player_count, created_at, updated_at, deleted) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        BrawlerBattleResultStatisticsCollectionEntity entity = entitiesToInsert.get(i);
                        ps.setLong(1, entity.getEventBrawlStarsId());
                        ps.setLong(2, entity.getBrawlerBrawlStarsId());
                        ps.setString(3, entity.getTierRange());
                        ps.setDate(4, Date.valueOf(entity.getBattleDate()));
                        ps.setLong(5, entity.getVictoryCount());
                        ps.setLong(6, entity.getDefeatCount());
                        ps.setLong(7, entity.getDrawCount());
                        ps.setLong(8, entity.getStarPlayerCount());
                        Instant createdAt = entity.getCreatedAt() != null ? entity.getCreatedAt().toInstant() : now;
                        Instant updatedAt = entity.getUpdatedAt() != null ? entity.getUpdatedAt().toInstant() : now;
                        ps.setTimestamp(9, Timestamp.from(createdAt));
                        ps.setTimestamp(10, Timestamp.from(updatedAt));
                        ps.setBoolean(11, entity.isDeleted());
                    }

                    @Override
                    public int getBatchSize() {
                        return entitiesToInsert.size();
                    }
                }
        );
    }

    private void updateEntities(List<BrawlerBattleResultStatisticsCollectionEntity> entitiesToUpdate, Instant now) {
        if (entitiesToUpdate.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate("UPDATE " + JpaUtils.getTableName(BrawlerBattleResultStatisticsCollectionEntity.class) +
                        " SET victory_count = ?, defeat_count = ?, draw_count = ?, star_player_count = ?, " +
                        "updated_at = ?, deleted = ? WHERE id = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        BrawlerBattleResultStatisticsCollectionEntity entity = entitiesToUpdate.get(i);
                        ps.setLong(1, entity.getVictoryCount());
                        ps.setLong(2, entity.getDefeatCount());
                        ps.setLong(3, entity.getDrawCount());
                        ps.setLong(4, entity.getStarPlayerCount());
                        Instant updatedAt = entity.getUpdatedAt() != null ? entity.getUpdatedAt().toInstant() : now;
                        ps.setTimestamp(5, Timestamp.from(updatedAt));
                        ps.setBoolean(6, entity.isDeleted());
                        ps.setLong(7, entity.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return entitiesToUpdate.size();
                    }
                }
        );
    }
}
