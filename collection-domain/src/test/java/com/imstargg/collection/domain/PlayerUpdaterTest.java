package com.imstargg.collection.domain;

import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntityFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
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
    void 플레이어정보를_업데이트한다() {
        // given
        var playerEntity = mock(PlayerCollectionEntity.class);
        var playerUpdater = playerUpdaterFactory.create(playerEntity);

        // when
        playerUpdater.updatePlayer();

        // then
        then(playerUpdateProcessor).should().update(playerEntity);
    }

    @Test
    void 플레이어를_업데이트한다() {
        // given
        var playerEntity = mock(PlayerCollectionEntity.class);
        var playerUpdater = playerUpdaterFactory.create(playerEntity);

        // when
        playerUpdater.update();

        // then
        then(playerUpdateProcessor).should().update(playerEntity);
        then(playerBattleUpdateProcessor).should().update(playerEntity);
        then(playerEntity).should().playerUpdated(clock, playerUpdater.getUpdatedBattleEntities());
    }

    @Test
    void 플레이어가_존재하지_않을_경우_삭제한다() {
        // given
        var playerEntity1 = new PlayerCollectionEntityFixture().build();
        var playerEntity2 = new PlayerCollectionEntityFixture().build();
        BrawlStarsClientException.NotFound ex = mock(BrawlStarsClientException.NotFound.class);
        willThrow(ex).given(playerUpdateProcessor).update(playerEntity1);
        willThrow(ex).given(playerBattleUpdateProcessor).update(playerEntity2);

        // when
        playerUpdaterFactory.create(playerEntity1).update();
        playerUpdaterFactory.create(playerEntity2).update();

        // then
        assertThat(playerEntity1.getStatus()).isEqualTo(PlayerStatus.DELETED);
        assertThat(playerEntity2.getStatus()).isEqualTo(PlayerStatus.DELETED);
    }

}