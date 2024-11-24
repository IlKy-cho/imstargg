package com.imstargg.storage.db.core;

import com.imstargg.storage.db.core.test.AbstractDataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BattleCollectionJpaRepositoryTest extends AbstractDataJpaTest {

    @Autowired
    private BattleCollectionJpaRepository battleCollectionJpaRepository;

    @Test
    void 플레이어들의_가장_최근_전적을_가져온다() {
        // given
        List<BattleCollectionEntity> battles = battleCollectionJpaRepository.saveAll(List.of(
                new BattleCollectionEntityFixture()
                        .playerId(1L)
                        .battleTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                        .build(),
                new BattleCollectionEntityFixture()
                        .playerId(1L)
                        .battleTime(LocalDateTime.of(2021, 1, 2, 0, 0))
                        .build(),
                new BattleCollectionEntityFixture()
                        .playerId(2L)
                        .battleTime(LocalDateTime.of(2021, 1, 3, 0, 0))
                        .build(),
                new BattleCollectionEntityFixture()
                        .playerId(2L)
                        .battleTime(LocalDateTime.of(2021, 1, 4, 0, 0))
                        .build(),
                new BattleCollectionEntityFixture()
                        .playerId(3L)
                        .battleTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                        .build()
        ));

        // when
        List<BattleCollectionEntity> result = battleCollectionJpaRepository.findAllLastBattleByPlayerIdIn(
                List.of(1L, 2L)
        );

        // then
        assertThat(result)
                .hasSize(2)
                .extracting(BattleCollectionEntity::getId)
                .containsExactlyInAnyOrder(battles.get(1).getId(), battles.get(3).getId());
    }
}