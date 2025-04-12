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
import java.util.Collection;
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
    public void saveAllWithNative(Collection<BrawlerBattleRankStatisticsCollectionEntity> entities) {
        List<BrawlerBattleRankStatisticsCollectionEntity> entitiesToInsert = entities.stream()
                .filter(entity -> entity.getId() == null)
                .toList();
        List<BrawlerBattleRankStatisticsCollectionEntity> entitiesToUpdate = entities.stream()
                .filter(entity -> entity.getId() != null)
                .toList();
        insertEntities(entitiesToInsert);
        updateEntities(entitiesToUpdate);
    }

    private void insertEntities(List<BrawlerBattleRankStatisticsCollectionEntity> entitiesToInsert) {
        if (entitiesToInsert.isEmpty()) {
            return;
        }
        jdbcTemplate.batchUpdate("INSERT INTO " + JpaUtils.getTableName(BrawlerBattleRankStatisticsCollectionEntity.class) +
                        " (event_brawlstars_id, brawler_brawlstars_id, tier_range, battle_date, rank_to_counts, deleted) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
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
                        ps.setBoolean(6, entity.isDeleted());
                    }

                    @Override
                    public int getBatchSize() {
                        return entitiesToInsert.size();
                    }
                }
        );
    }

    private void updateEntities(List<BrawlerBattleRankStatisticsCollectionEntity> entitiesToUpdate) {
        if (entitiesToUpdate.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate("UPDATE " + JpaUtils.getTableName(BrawlerBattleRankStatisticsCollectionEntity.class) +
                        " SET rank_to_counts = ?, deleted = ? WHERE brawler_battle_rank_stats_id = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        BrawlerBattleRankStatisticsCollectionEntity entity = entitiesToUpdate.get(i);
                        try {
                            ps.setString(1, objectMapper.writeValueAsString(entity.getRankToCounts()));
                        } catch (JsonProcessingException e) {
                            throw new UncheckedIOException(e);
                        }
                        ps.setBoolean(2, entity.isDeleted());
                        ps.setLong(3, entity.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return entitiesToUpdate.size();
                    }
                }
        );
    }

}
