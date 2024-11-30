package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerTagFinderWithLocalCache;
import com.imstargg.core.enums.UnknownPlayerStatus;
import com.imstargg.storage.db.core.BattlePlayerCollectionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class NewPlayerFindJobItemProcessorTest {

    private NewPlayerFindJobItemProcessor newPlayerFindJobItemProcessor;

    private Clock clock;

    private PlayerTagFinderWithLocalCache playerTagFinder;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        playerTagFinder = mock(PlayerTagFinderWithLocalCache.class);
        newPlayerFindJobItemProcessor = new NewPlayerFindJobItemProcessor(clock, playerTagFinder);
    }

    @Test
    void 태그가_존재하면_null을_반환한다() throws Exception {
        // given
        given(playerTagFinder.exists("player1")).willReturn(true);
        var battlePlayer = mock(BattlePlayerCollectionEntity.class);
        given(battlePlayer.getBrawlStarsTag()).willReturn("player1");

        // when
        // then
        assertThat(newPlayerFindJobItemProcessor.process(battlePlayer))
                .isNull();
    }

    @Test
    void 태그가_존재하지_않으면_알수없는_플레이어로_반환한다() throws Exception {
        // given
        given(playerTagFinder.exists("player2")).willReturn(false);
        var battlePlayer = mock(BattlePlayerCollectionEntity.class);
        given(battlePlayer.getBrawlStarsTag()).willReturn("player2");

        // when
        // then
        var result = newPlayerFindJobItemProcessor.process(battlePlayer);

        assertThat(result).isNotNull();
        assertThat(result.getBrawlStarsTag()).isEqualTo("player2");
        assertThat(result.getStatus()).isEqualTo(UnknownPlayerStatus.UPDATE_NEW);
    }
}