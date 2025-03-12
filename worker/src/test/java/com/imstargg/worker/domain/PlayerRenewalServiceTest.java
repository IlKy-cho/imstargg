package com.imstargg.worker.domain;

import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.core.enums.PlayerRenewalStatus;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerRenewalCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PlayerRenewalServiceTest {

    @InjectMocks
    private PlayerRenewalService playerRenewalService;

    @Mock
    private PlayerFinder playerFinder;

    @Mock
    private PlayerRenewalUpdater playerRenewalUpdater;

    @Mock
    private PlayerRenewalReader playerRenewalReader;

    @Mock
    private PlayerRenewalProcessor playerRenewalProcessor;

    @Captor
    private ArgumentCaptor<PlayerRenewalCollectionEntity> renewalCaptor;

    @Test
    void 갱신_정보가_PENDING_상태가_아니면_아무_처리하지_않는다() {
        // given
        String tag = "#12345";
        PlayerRenewalCollectionEntity renewal = mock(PlayerRenewalCollectionEntity.class);
        given(playerRenewalReader.get(tag)).willReturn(renewal);
        given(renewal.getStatus()).willReturn(PlayerRenewalStatus.COMPLETE);

        // when
        playerRenewalService.renew(tag);

        // then
        then(playerRenewalUpdater).shouldHaveNoInteractions();
        then(playerFinder).shouldHaveNoInteractions();
        then(playerRenewalProcessor).shouldHaveNoInteractions();
    }

    @Test
    void 플레이어를_갱신한다() {
        // given
        String tag = "#12345";
        PlayerRenewalCollectionEntity renewal = mock(PlayerRenewalCollectionEntity.class);
        PlayerCollectionEntity player = mock(PlayerCollectionEntity.class);
        given(playerRenewalReader.get(tag)).willReturn(renewal);
        given(renewal.getStatus()).willReturn(PlayerRenewalStatus.PENDING);
        given(playerFinder.findPlayer(tag)).willReturn(Optional.of(player));

        // when
        playerRenewalService.renew(tag);

        // then
        InOrder inOrder = inOrder(playerRenewalUpdater, playerRenewalProcessor);
        inOrder.verify(playerRenewalUpdater).executing(renewal);
        inOrder.verify(playerRenewalProcessor).renewPlayer(player);
        inOrder.verify(playerRenewalUpdater).complete(renewal);
    }

    @Test
    void 새_플레이어를_갱신한다() {
        // given
        String tag = "#12345";
        PlayerRenewalCollectionEntity renewal = mock(PlayerRenewalCollectionEntity.class);
        UnknownPlayerCollectionEntity unknownPlayer = mock(UnknownPlayerCollectionEntity.class);
        given(playerRenewalReader.get(tag)).willReturn(renewal);
        given(renewal.getStatus()).willReturn(PlayerRenewalStatus.PENDING);
        given(playerFinder.findPlayer(tag)).willReturn(Optional.empty());
        given(playerFinder.findUnknownPlayer(tag)).willReturn(Optional.of(unknownPlayer));

        // when
        playerRenewalService.renew(tag);

        // then
        InOrder inOrder = inOrder(playerRenewalUpdater, playerRenewalProcessor);
        inOrder.verify(playerRenewalUpdater).executing(renewal);
        inOrder.verify(playerRenewalProcessor).renewNewPlayer(unknownPlayer);
        inOrder.verify(playerRenewalUpdater).complete(renewal);
    }

    @Test
    void 플레이어와_새플레이어_모두_존재하지_않을경우_예외를_발생시킨다() {
        // given
        String tag = "#12345";
        PlayerRenewalCollectionEntity renewal = mock(PlayerRenewalCollectionEntity.class);
        given(playerRenewalReader.get(tag)).willReturn(renewal);
        given(renewal.getStatus()).willReturn(PlayerRenewalStatus.PENDING);
        given(playerFinder.findPlayer(tag)).willReturn(Optional.empty());
        given(playerFinder.findUnknownPlayer(tag)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> playerRenewalService.renew(tag))
                .isInstanceOf(IllegalStateException.class);
        then(playerRenewalUpdater).should().executing(renewal);
        then(playerRenewalUpdater).should().failed(renewal);
    }

    @Test
    void 브롤스타즈가_점검중이면_갱신상태를_점검중으로_변경한다() {
        // given
        String tag = "#12345";
        PlayerRenewalCollectionEntity renewal = mock(PlayerRenewalCollectionEntity.class);
        given(playerRenewalReader.get(tag)).willReturn(renewal);
        given(renewal.getStatus()).willReturn(PlayerRenewalStatus.PENDING);
        given(playerFinder.findPlayer(tag)).willThrow(new BrawlStarsClientException.InMaintenance("점검중"));

        // when
        playerRenewalService.renew(tag);

        // then
        then(playerRenewalUpdater).should().executing(renewal);
        then(playerRenewalUpdater).should().inMaintenance(renewal);
    }

    @Test
    void 이미_갱신중이면_아무처리_하지_않는다() {
        // given
        String tag = "#12345";
        PlayerRenewalCollectionEntity renewal = mock(PlayerRenewalCollectionEntity.class);
        given(playerRenewalReader.get(tag)).willReturn(renewal);
        given(renewal.getStatus()).willReturn(PlayerRenewalStatus.PENDING);
        doThrow(OptimisticLockingFailureException.class)
                .when(playerRenewalUpdater).executing(renewal);

        // when
        playerRenewalService.renew(tag);

        // then
        then(playerRenewalUpdater).should().executing(renewal);
        then(playerFinder).shouldHaveNoInteractions();
        then(playerRenewalProcessor).shouldHaveNoInteractions();
    }

    @Test
    void 예외가_발생하면_갱신상태를_실패로_변경한다() {
        // given
        String tag = "#12345";
        PlayerRenewalCollectionEntity renewal = mock(PlayerRenewalCollectionEntity.class);
        given(playerRenewalReader.get(tag)).willReturn(renewal);
        given(renewal.getStatus()).willReturn(PlayerRenewalStatus.PENDING);
        given(playerFinder.findPlayer(tag)).willThrow(new RuntimeException("에러 발생"));

        // when & then
        assertThatThrownBy(() -> playerRenewalService.renew(tag))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("에러 발생");

        then(playerRenewalUpdater).should().executing(renewal);
        then(playerRenewalUpdater).should().failed(renewal);
    }
}