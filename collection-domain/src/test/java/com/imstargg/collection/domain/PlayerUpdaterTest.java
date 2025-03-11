package com.imstargg.collection.domain;

import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PlayerUpdaterTest {

    private PlayerUpdaterFactory playerUpdaterFactory;

    private Clock clock;

    @Mock
    private PlayerUpdateProcessor playerUpdateProcessor;

    @Mock
    private PlayerBattleUpdateProcessor playerBattleUpdateProcessor;

    @BeforeEach
    void setUp() {
        this.clock = Clock.fixed(Instant.now(), Clock.systemDefaultZone().getZone());
        playerUpdaterFactory = new PlayerUpdaterFactory(
                clock, playerUpdateProcessor, playerBattleUpdateProcessor
        );
    }

    @Test
    void 플레이어를_업데이트한다() {
        // given
        var playerEntity = mock(PlayerCollectionEntity.class);
        var playerUpdater = playerUpdaterFactory.create(playerEntity);

        // when
        playerUpdater.updatePlayer();

        // then
        then(playerUpdateProcessor).should().update(playerEntity);
    }

    @Test
    void 업데이트를_하면_경쟁전_티어를_업데이트한다() {
        // given
    }

    @Test
    void 최근_전투일시를_업데이트한다() {

    }

    @Test
    void 플레이어_상태를_업데이트한다() {

    }

    @Test
    void AI_플레이어를_검열한다() {

    }
}