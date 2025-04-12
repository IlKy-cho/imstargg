package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.MySqlContainerTest;
import com.imstargg.storage.db.core.test.CleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Tag("develop")
class BrawlerPairBattleResultStatisticsCollectionJpaRepositoryMySqlTest extends MySqlContainerTest {

    @Autowired
    private CleanUp cleanUp;

    @Autowired
    private BrawlerPairBattleResultStatisticsCollectionJpaRepository repository;

    @AfterEach
    void tearDown() {
        cleanUp.all();
    }

    @Test
    void mysql_네이티브쿼리로_엔티티를_저장한다() {
        // given
        var entity = new BrawlerPairBattleResultStatisticsCollectionEntity(
                1L,
                2L,
                TrophyRange.TROPHY_0_PLUS,
                LocalDate.of(2025, 4, 3),
                3L
        );
        entity.countUp(BattleResult.VICTORY);
        entity.countUp(BattleResult.VICTORY);
        entity.countUp(BattleResult.DEFEAT);

        // when
        repository.saveAllWithNative(List.of(entity));

        // then
        var result = repository.findAll().getFirst();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getEventBrawlStarsId()).isEqualTo(1L);
        assertThat(result.getBrawlerBrawlStarsId()).isEqualTo(2L);
        assertThat(result.getTierRange()).isEqualTo(TrophyRange.TROPHY_0_PLUS.name());
        assertThat(result.getBattleDate()).isEqualTo(LocalDate.of(2025, 4, 3));
        assertThat(result.getPairBrawlerBrawlStarsId()).isEqualTo(3L);
        assertThat(result.getVictoryCount()).isEqualTo(2L);
        assertThat(result.getDefeatCount()).isEqualTo(1L);
        assertThat(result.getDrawCount()).isEqualTo(0L);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        assertThat(result.isDeleted()).isFalse();
    }

    @Test
    void mysql_네이티브쿼리로_엔티티를_업데이트한다() {
        // given
        var entity = new BrawlerPairBattleResultStatisticsCollectionEntity(
                1L,
                2L,
                TrophyRange.TROPHY_0_PLUS,
                LocalDate.of(2025, 4, 3),
                3L
        );
        repository.saveAllWithNative(List.of(entity));

        // when
        var insertedEntity = repository.findAll().getFirst();
        insertedEntity.countUp(BattleResult.VICTORY);
        insertedEntity.countUp(BattleResult.DRAW);
        repository.saveAllWithNative(List.of(insertedEntity));

        // then
        var result = repository.findAll().getFirst();
        assertThat(result.getId()).isEqualTo(insertedEntity.getId());
        assertThat(result.getEventBrawlStarsId()).isEqualTo(1L);
        assertThat(result.getBrawlerBrawlStarsId()).isEqualTo(2L);
        assertThat(result.getTierRange()).isEqualTo(TrophyRange.TROPHY_0_PLUS.name());
        assertThat(result.getBattleDate()).isEqualTo(LocalDate.of(2025, 4, 3));
        assertThat(result.getPairBrawlerBrawlStarsId()).isEqualTo(3L);
        assertThat(result.getVictoryCount()).isEqualTo(1L);
        assertThat(result.getDefeatCount()).isEqualTo(0L);
        assertThat(result.getDrawCount()).isEqualTo(1L);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }
}