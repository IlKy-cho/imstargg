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

public class BrawlerPairBattleRankStatisticsCollectionJpaRepositoryCustomImpl
        implements BrawlerPairBattleRankStatisticsCollectionJpaRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BrawlerPairBattleRankStatisticsCollectionJpaRepositoryCustomImpl(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public void saveAllWithNative(Collection<BrawlerPairBattleRankStatisticsCollectionEntity> entities) {
        List<BrawlerPairBattleRankStatisticsCollectionEntity> entitiesToInsert = entities.stream()
                .filter(entity -> entity.getId() == null)
                .toList();
        List<BrawlerPairBattleRankStatisticsCollectionEntity> entitiesToUpdate = entities.stream()
                .filter(entity -> entity.getId() != null)
                .toList();
        insertEntities(entitiesToInsert);
        updateEntities(entitiesToUpdate);
    }

    private void insertEntities(List<BrawlerPairBattleRankStatisticsCollectionEntity> entitiesToInsert) {
        if (entitiesToInsert.isEmpty()) {
            return;
        }
        jdbcTemplate.batchUpdate("INSERT INTO " + JpaUtils.getTableName(BrawlerPairBattleRankStatisticsCollectionEntity.class) +
                        " (event_brawlstars_id, brawler_brawlstars_id, tier_range, battle_date, " +
                        "pair_brawler_brawlstars_id, rank_to_counts, deleted) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        BrawlerPairBattleRankStatisticsCollectionEntity entity = entitiesToInsert.get(i);
                        ps.setLong(1, entity.getEventBrawlStarsId());
                        ps.setLong(2, entity.getBrawlerBrawlStarsId());
                        ps.setString(3, entity.getTierRange());
                        ps.setDate(4, Date.valueOf(entity.getBattleDate()));
                        ps.setLong(5, entity.getPairBrawlerBrawlStarsId());
                        try {
                            ps.setString(6, objectMapper.writeValueAsString(entity.getRankToCounts()));
                        } catch (JsonProcessingException e) {
                            throw new UncheckedIOException(e);
                        }
                        ps.setBoolean(7, entity.isDeleted());
                    }

                    @Override
                    public int getBatchSize() {
                        return entitiesToInsert.size();
                    }
                }
        );
    }

    private void updateEntities(List<BrawlerPairBattleRankStatisticsCollectionEntity> entitiesToUpdate) {
        if (entitiesToUpdate.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate("UPDATE " + JpaUtils.getTableName(BrawlerPairBattleRankStatisticsCollectionEntity.class) +
                        " SET rank_to_counts = ?, deleted = ? WHERE brawler_pair_battle_rank_stats_id = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        BrawlerPairBattleRankStatisticsCollectionEntity entity = entitiesToUpdate.get(i);
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
