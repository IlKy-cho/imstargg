package com.imstargg.storage.db.core.statistics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imstargg.storage.db.core.support.JpaUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.UncheckedIOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.List;

public class BrawlerBattleRankStatisticsCollectionJpaRepositoryCustomImpl
        implements BrawlerBattleRankStatisticsCollectionJpaRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BrawlerBattleRankStatisticsCollectionJpaRepositoryCustomImpl(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public void saveAllWithNative(Clock clock, List<BrawlerBattleRankStatisticsCollectionEntity> entities) {
        List<BrawlerBattleRankStatisticsCollectionEntity> entitiesToInsert = entities.stream()
                .filter(entity -> entity.getId() == null)
                .toList();
        List<BrawlerBattleRankStatisticsCollectionEntity> entitiesToUpdate = entities.stream()
                .filter(entity -> entity.getId() != null)
                .toList();
        Instant now = clock.instant();
        insertEntities(entitiesToInsert, now);
        updateEntities(entitiesToUpdate, now);
    }

    private void insertEntities(List<BrawlerBattleRankStatisticsCollectionEntity> entitiesToInsert, Instant now) {
        if (entitiesToInsert.isEmpty()) {
            return;
        }
        jdbcTemplate.batchUpdate("INSERT INTO " + JpaUtils.getTableName(BrawlerBattleRankStatisticsCollectionEntity.class) +
                        " (event_brawlstars_id, brawler_brawlstars_id, tier_range, battle_date, rank_to_counts, created_at, updated_at, deleted) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {

                        BrawlerBattleRankStatisticsCollectionEntity entity = entitiesToInsert.get(i);
                        ps.setLong(1, entity.getEventBrawlStarsId());
                        ps.setLong(2, entity.getBrawlerBrawlStarsId());
                        ps.setString(3, entity.getTierRange());
                        ps.setDate(4, Date.valueOf(entity.getBattleDate()));
                        try {
                            ps.setString(5, objectMapper.writeValueAsString(entity.getRankToCounts()));
                        } catch (JsonProcessingException e) {
                            throw new UncheckedIOException(e);
                        }
                        Instant createdAt = entity.getCreatedAt() != null ? entity.getCreatedAt().toInstant() : now;
                        Instant updatedAt = entity.getUpdatedAt() != null ? entity.getUpdatedAt().toInstant() : now;
                        ps.setTimestamp(6, Timestamp.from(createdAt));
                        ps.setTimestamp(7, Timestamp.from(updatedAt));
                        ps.setBoolean(8, entity.isDeleted());
                    }

                    @Override
                    public int getBatchSize() {
                        return entitiesToInsert.size();
                    }
                }
        );
    }

    private void updateEntities(List<BrawlerBattleRankStatisticsCollectionEntity> entitiesToUpdate, Instant now) {
        if (entitiesToUpdate.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate("UPDATE " + JpaUtils.getTableName(BrawlerBattleRankStatisticsCollectionEntity.class) +
                        " SET rank_to_counts = ?, updated_at = ?, deleted = ? WHERE id = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        BrawlerBattleRankStatisticsCollectionEntity entity = entitiesToUpdate.get(i);
                        Instant updatedAt = entity.getUpdatedAt() != null ? entity.getUpdatedAt().toInstant() : now;
                        try {
                            ps.setString(1, objectMapper.writeValueAsString(entity.getRankToCounts()));
                        } catch (JsonProcessingException e) {
                            throw new UncheckedIOException(e);
                        }
                        ps.setTimestamp(2, Timestamp.from(updatedAt));
                        ps.setBoolean(3, entity.isDeleted());
                        ps.setLong(4, entity.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return entitiesToUpdate.size();
                    }
                }
        );
    }

}
