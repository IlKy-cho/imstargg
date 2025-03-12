package com.imstargg.worker.domain;

import com.imstargg.storage.db.core.PlayerRenewalCollectionEntity;
import com.imstargg.storage.db.core.PlayerRenewalCollectionJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PlayerRenewalUpdaterTest {

    @InjectMocks
    private PlayerRenewalUpdater playerRenewalUpdater;

    @Mock
    private PlayerRenewalCollectionJpaRepository playerRenewalCollectionJpaRepository;

    @Test
    void executing_상태로_변경하고_저장한다() {
        // given
        PlayerRenewalCollectionEntity renewal = mock(PlayerRenewalCollectionEntity.class);

        // when
        playerRenewalUpdater.executing(renewal);

        // then
        then(renewal).should().executing();
        then(playerRenewalCollectionJpaRepository).should().save(renewal);
    }

    @Test
    void complete_상태로_변경하고_저장한다() {
        // given
        PlayerRenewalCollectionEntity renewal = mock(PlayerRenewalCollectionEntity.class);

        // when
        playerRenewalUpdater.complete(renewal);

        // then
        then(renewal).should().complete();
        then(playerRenewalCollectionJpaRepository).should().save(renewal);
    }

    @Test
    void inMaintenance_상태로_변경하고_저장한다() {
        // given
        PlayerRenewalCollectionEntity renewal = mock(PlayerRenewalCollectionEntity.class);

        // when
        playerRenewalUpdater.inMaintenance(renewal);

        // then
        then(renewal).should().inMaintenance();
        then(playerRenewalCollectionJpaRepository).should().save(renewal);
    }

    @Test
    void failed_상태로_변경하고_저장한다() {
        // given
        PlayerRenewalCollectionEntity renewal = mock(PlayerRenewalCollectionEntity.class);

        // when
        playerRenewalUpdater.failed(renewal);

        // then
        then(renewal).should().failed();
        then(playerRenewalCollectionJpaRepository).should().save(renewal);
    }
}