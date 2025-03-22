package com.imstargg.collection.domain;


import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PlayerBattleUpdateProcessorTest {

    @InjectMocks
    private PlayerBattleUpdateProcessor playerBattleUpdateProcessor;

    @Mock
    private BrawlStarsClient brawlStarsClient;

    @Mock
    private BattleCollectionEntityFactory battleEntityFactory;

    @Test
    void 전투를_업데이트한다() {
        // given
        var playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.getBrawlStarsTag()).willReturn("#12345");
        var battleResponse1 = mock(BattleResponse.class);
        var battleResponse2 = mock(BattleResponse.class);
        var battleResponse3 = mock(BattleResponse.class);

        given(brawlStarsClient.getPlayerRecentBattles("#12345"))
                .willReturn(new ListResponse<>(List.of(battleResponse1, battleResponse2, battleResponse3), null));

        var battleEntity1 = mock(BattleCollectionEntity.class);
        given(battleEntity1.getBattleTime()).willReturn(ZonedDateTime.of(2025, 3, 11, 0, 0, 0, 0, ZoneId.systemDefault()).toOffsetDateTime());
        var battleEntity2 = mock(BattleCollectionEntity.class);
        given(battleEntity2.getBattleTime()).willReturn(ZonedDateTime.of(2025, 3, 10, 0, 0, 0, 0, ZoneId.systemDefault()).toOffsetDateTime());
        var battleEntity3 = mock(BattleCollectionEntity.class);
        given(battleEntity3.getBattleTime()).willReturn(ZonedDateTime.of(2025, 3, 9, 0, 0, 0, 0, ZoneId.systemDefault()).toOffsetDateTime());
        given(battleEntityFactory.create(playerEntity, battleResponse1)).willReturn(battleEntity1);
        given(battleEntityFactory.create(playerEntity, battleResponse2)).willReturn(battleEntity2);
        given(battleEntityFactory.create(playerEntity, battleResponse3)).willReturn(battleEntity3);

        // when
        List<BattleCollectionEntity> battleEntities = playerBattleUpdateProcessor.update(playerEntity);

        // then
        assertThat(battleEntities).containsExactly(battleEntity3, battleEntity2, battleEntity1);
    }

    @Test
    void 플레이어의_최근_업데이트된_전투일시이후의_전투만_반환한다() {
        // given
        var playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.getLatestBattleTime())
                .willReturn(ZonedDateTime.of(2025, 3, 9, 0, 0, 0, 0, ZoneId.systemDefault()).toOffsetDateTime());
        given(playerEntity.getBrawlStarsTag()).willReturn("#12345");
        var battleResponse1 = mock(BattleResponse.class);
        given(battleResponse1.battleTime())
                .willReturn(ZonedDateTime.of(2025, 3, 11, 0, 0, 0, 0, ZoneId.systemDefault()).toOffsetDateTime());
        var battleResponse2 = mock(BattleResponse.class);
        given(battleResponse2.battleTime())
                .willReturn(ZonedDateTime.of(2025, 3, 10, 0, 0, 0, 0, ZoneId.systemDefault()).toOffsetDateTime());
        var battleResponse3 = mock(BattleResponse.class);
        given(battleResponse3.battleTime())
                .willReturn(ZonedDateTime.of(2025, 3, 9, 0, 0, 0, 0, ZoneId.systemDefault()).toOffsetDateTime());

        given(brawlStarsClient.getPlayerRecentBattles("#12345"))
                .willReturn(new ListResponse<>(List.of(battleResponse1, battleResponse2, battleResponse3), null));

        var battleEntity1 = mock(BattleCollectionEntity.class);
        given(battleEntity1.getBattleTime()).willReturn(ZonedDateTime.of(2025, 3, 11, 0, 0, 0, 0, ZoneId.systemDefault()).toOffsetDateTime());
        var battleEntity2 = mock(BattleCollectionEntity.class);
        given(battleEntity2.getBattleTime()).willReturn(ZonedDateTime.of(2025, 3, 10, 0, 0, 0, 0, ZoneId.systemDefault()).toOffsetDateTime());
        given(battleEntityFactory.create(playerEntity, battleResponse1)).willReturn(battleEntity1);
        given(battleEntityFactory.create(playerEntity, battleResponse2)).willReturn(battleEntity2);

        // when
        List<BattleCollectionEntity> battleEntities = playerBattleUpdateProcessor.update(playerEntity);

        // then
        assertThat(battleEntities).containsExactly(battleEntity2, battleEntity1);
    }


}