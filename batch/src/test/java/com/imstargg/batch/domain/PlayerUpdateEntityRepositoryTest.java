package com.imstargg.batch.domain;

import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityPlayer;
import com.imstargg.storage.db.core.BattleCollectionJpaRepository;
import com.imstargg.storage.db.core.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.PlayerBrawlerCollectionJpaRepository;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Limit;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PlayerUpdateEntityRepositoryTest {

    @InjectMocks
    private PlayerUpdateEntityRepository playerUpdateEntityRepository;
    @Mock
    private PlayerCollectionJpaRepository playerCollectionJpaRepository;
    @Mock
    private PlayerBrawlerCollectionJpaRepository playerBrawlerCollectionJpaRepository;
    @Mock
    private BattleCollectionJpaRepository battleCollectionJpaRepository;

    @Test
    void 업데이트할_플레이어를_조회한다() {
        // given
        int size = 10;
        PlayerCollectionEntity player1 = mock(PlayerCollectionEntity.class);
        given(player1.getId()).willReturn(1L);
        PlayerCollectionEntity player2 = mock(PlayerCollectionEntity.class);
        given(player2.getId()).willReturn(2L);
        given(playerCollectionJpaRepository
                .findAllWithOptimisticLockByDeletedFalseAndStatusInOrderByUpdateWeight(
                        List.of(PlayerStatus.UPDATED, PlayerStatus.UPDATING),
                        Limit.of(size)
                )).willReturn(List.of(player1, player2));

        PlayerBrawlerCollectionEntity player1Brawler1 = mock(PlayerBrawlerCollectionEntity.class);
        PlayerBrawlerCollectionEntity player1Brawler2 = mock(PlayerBrawlerCollectionEntity.class);
        given(player1Brawler1.getPlayerId()).willReturn(1L);
        given(player1Brawler2.getPlayerId()).willReturn(1L);
        given(playerBrawlerCollectionJpaRepository.findAllByPlayerIdIn(List.of(1L, 2L))).willReturn(List.of(
                player1Brawler1, player1Brawler2
        ));

        BattleCollectionEntity battle1 = mock(BattleCollectionEntity.class);
        BattleCollectionEntityPlayer battle1Player1 = mock(BattleCollectionEntityPlayer.class);
        given(battle1Player1.getPlayerId()).willReturn(1L);
        given(battle1.getPlayer()).willReturn(battle1Player1);
        given(battleCollectionJpaRepository.findAllLastBattleByPlayerIdIn(List.of(1L, 2L))).willReturn(List.of(
                battle1
        ));

        // when
        List<PlayerToUpdateEntity> playerToUpdateEntities = playerUpdateEntityRepository.find(size);

        // then
        assertThat(playerToUpdateEntities).hasSize(2);
        assertThat(playerToUpdateEntities.get(0).playerEntity().getId()).isEqualTo(1L);
        assertThat(playerToUpdateEntities.get(0).playerBrawlerEntities()).hasSize(2);
        assertThat(playerToUpdateEntities.get(0).lastBattleEntity()).isPresent();
        then(player1).should().setStatus(PlayerStatus.UPDATING);

        assertThat(playerToUpdateEntities.get(1).playerEntity().getId()).isEqualTo(2L);
        assertThat(playerToUpdateEntities.get(1).playerBrawlerEntities()).isEmpty();
        assertThat(playerToUpdateEntities.get(1).lastBattleEntity()).isEmpty();
        then(player2).should().setStatus(PlayerStatus.UPDATING);
    }
}