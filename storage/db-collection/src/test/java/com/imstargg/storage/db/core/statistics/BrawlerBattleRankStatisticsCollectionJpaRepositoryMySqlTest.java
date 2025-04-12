package com.imstargg.storage.db.core.statistics;

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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Tag("develop")
class BrawlerBattleRankStatisticsCollectionJpaRepositoryMySqlTest extends MySqlContainerTest {

    @Autowired
    private CleanUp cleanUp;

    @Autowired
    private BrawlerBattleRankStatisticsCollectionJpaRepository repository;

    @AfterEach
    void tearDown() {
        cleanUp.all();
    }

    @Test
    void mysql_네이티브쿼리로_엔티티를_저장한다() {
        // given
        var entity = new BrawlerBattleRankStatisticsCollectionEntity(
                1L,
                2L,
                TrophyRange.TROPHY_0_PLUS,
                LocalDate.of(2025, 4, 3)
        );
        entity.countUp(1);
        entity.countUp(2);

        // when
        repository.saveAllWithNative(List.of(entity));

        // then
        var result = repository.findAll().getFirst();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getEventBrawlStarsId()).isEqualTo(1L);
        assertThat(result.getBrawlerBrawlStarsId()).isEqualTo(2L);
        assertThat(result.getTierRange()).isEqualTo(TrophyRange.TROPHY_0_PLUS.name());
        assertThat(result.getBattleDate()).isEqualTo(LocalDate.of(2025, 4, 3));
        assertThat(result.getRankToCounts()).isEqualTo(Map.of(
                1, 1L,
                2, 1L
        ));
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        assertThat(result.isDeleted()).isFalse();
    }

    @Test
    void mysql_네이티브쿼리로_엔티티를_업데이트한다() {
        // given
        var entity = new BrawlerBattleRankStatisticsCollectionEntity(
                1L,
                2L,
                TrophyRange.TROPHY_0_PLUS,
                LocalDate.of(2025, 4, 3)
        );
        repository.saveAllWithNative(List.of(entity));

        // when
        var insertedEntity = repository.findAll().getFirst();
        insertedEntity.countUp(1);
        insertedEntity.countUp(2);
        repository.saveAllWithNative(List.of(insertedEntity));

        // then
        var result = repository.findAll().getFirst();
        assertThat(result.getId()).isEqualTo(insertedEntity.getId());
        assertThat(result.getEventBrawlStarsId()).isEqualTo(1L);
        assertThat(result.getBrawlerBrawlStarsId()).isEqualTo(2L);
        assertThat(result.getTierRange()).isEqualTo(TrophyRange.TROPHY_0_PLUS.name());
        assertThat(result.getBattleDate()).isEqualTo(LocalDate.of(2025, 4, 3));
        assertThat(result.getRankToCounts()).isEqualTo(Map.of(
                1, 1L,
                2, 1L
        ));
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt().compareTo(result.getCreatedAt())).isGreaterThan(0);
    }
}