package com.imstargg.worker.domain;

import com.imstargg.collection.domain.PlayerUpdaterFactory;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import com.imstargg.storage.db.core.player.UnknownPlayerCollectionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PlayerRenewalProcessorTest {

    @InjectMocks
    private PlayerRenewalProcessor playerRenewalProcessor;

    @Mock
    private PlayerUpdater playerUpdater;

    @Mock
    private PlayerUpdaterFactory playerUpdaterFactory;

    @Test
    void 플레이어를_업데이트한다() {
        // given
        PlayerCollectionEntity player = mock(PlayerCollectionEntity.class);
        given(player.getBrawlStarsTag()).willReturn("#12345");
        var updater = mock(com.imstargg.collection.domain.PlayerUpdater.class);
        given(playerUpdaterFactory.create(player)).willReturn(updater);

        PlayerCollectionEntity updatedPlayer = mock(PlayerCollectionEntity.class);
        BattleCollectionEntity updatedBattle = mock(BattleCollectionEntity.class);
        given(updater.getPlayerEntity()).willReturn(updatedPlayer);
        given(updater.getUpdatedBattleEntities()).willReturn(List.of(updatedBattle));

        // when
        playerRenewalProcessor.renewPlayer(player);

        // then
        then(updater).should().update();
        then(playerUpdater).should().update(updatedPlayer, List.of(updatedBattle));
    }

    @Test
    void 신규_플레이어를_업데이트한다() {
        // given
        var unknownPlayer = mock(UnknownPlayerCollectionEntity.class);
        given(unknownPlayer.getBrawlStarsTag()).willReturn("#12345");
        var updater = mock(com.imstargg.collection.domain.PlayerUpdater.class);

        ArgumentCaptor<PlayerCollectionEntity> playerCaptor = ArgumentCaptor.forClass(PlayerCollectionEntity.class);
        given(playerUpdaterFactory.create(playerCaptor.capture())).willReturn(updater);

        PlayerCollectionEntity updatedPlayer = mock(PlayerCollectionEntity.class);
        given(updatedPlayer.getStatus()).willReturn(PlayerStatus.PLAYER_UPDATED);
        BattleCollectionEntity updatedBattle = mock(BattleCollectionEntity.class);
        given(updater.getPlayerEntity()).willReturn(updatedPlayer);
        given(updater.getUpdatedBattleEntities()).willReturn(List.of(updatedBattle));

        // when
        playerRenewalProcessor.renewNewPlayer(unknownPlayer);

        // then
        PlayerCollectionEntity capturedPlayer = playerCaptor.getValue();
        assertThat(capturedPlayer.getBrawlStarsTag()).isEqualTo("#12345");
        assertThat(capturedPlayer.getId()).isNull();
        then(updater).should().update();
        then(playerUpdater).should().update(unknownPlayer, updatedPlayer, List.of(updatedBattle));
    }

    @Test
    void 신규_플레이어가_삭제된_플레이어인_경우_저장하지않고_notfound처리() {
        // given
        var unknownPlayer = mock(UnknownPlayerCollectionEntity.class);
        given(unknownPlayer.getBrawlStarsTag()).willReturn("#12345");
        var updater = mock(com.imstargg.collection.domain.PlayerUpdater.class);
        given(playerUpdaterFactory.create(any(PlayerCollectionEntity.class))).willReturn(updater);
        PlayerCollectionEntity updatedPlayer = mock(PlayerCollectionEntity.class);
        given(updatedPlayer.getStatus()).willReturn(PlayerStatus.DELETED);
        given(updater.getPlayerEntity()).willReturn(updatedPlayer);

        // when
        playerRenewalProcessor.renewNewPlayer(unknownPlayer);

        // then
        then(updater).should().update();
        then(unknownPlayer).should().notFound();
        then(playerUpdater).should().update(unknownPlayer);
    }
}