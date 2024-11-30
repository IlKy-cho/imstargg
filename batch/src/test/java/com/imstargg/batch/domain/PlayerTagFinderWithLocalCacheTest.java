package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.PlayerCollectionJpaRepository;
import com.imstargg.storage.db.core.UnknownPlayerCollectionJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PlayerTagFinderWithLocalCacheTest {

    @InjectMocks
    private PlayerTagFinderWithLocalCache playerTagFinder;

    @Mock
    private PlayerCollectionJpaRepository playerRepository;

    @Mock
    private UnknownPlayerCollectionJpaRepository unknownPlayerRepository;

    @Test
    void 태그가_존재하는지_확인한다() {
        // given
        given(playerRepository.existsByBrawlStarsTag("player1"))
                .willReturn(true);

        given(playerRepository.existsByBrawlStarsTag("player2"))
                .willReturn(false);
        given(unknownPlayerRepository.existsByBrawlStarsTag("player2"))
                .willReturn(true);

        given(playerRepository.existsByBrawlStarsTag("player3"))
                .willReturn(false);
        given(unknownPlayerRepository.existsByBrawlStarsTag("player3"))
                .willReturn(false);


        // when
        // then
        assertThat(playerTagFinder.exists("player1")).isTrue();
        assertThat(playerTagFinder.exists("player2")).isTrue();
        assertThat(playerTagFinder.exists("player3")).isFalse();
    }

    @Test
    void 캐시에_태그가_존재하면_캐시데이터를_확인한다() {
        // given
        given(playerRepository.existsByBrawlStarsTag("existsTag"))
                .willReturn(true);

        // when
        playerTagFinder.exists("existsTag");

        // then
        assertThat(playerTagFinder.exists("existsTag")).isTrue();
        then(playerRepository)
                .should(times(1))
                .existsByBrawlStarsTag("existsTag");
    }

    @Test
    void 존재하지_않는_태그도_한번_조회후에는_존재함으로_본다() {
        // 이후에 저장됐을 것으로 봄
        // given
        given(playerRepository.existsByBrawlStarsTag("player1"))
                .willReturn(false);
        given(unknownPlayerRepository.existsByBrawlStarsTag("player1"))
                .willReturn(false);

        // when
        boolean result1 = playerTagFinder.exists("player1");
        boolean result2 = playerTagFinder.exists("player1");

        // then
        assertThat(result1).isFalse();
        assertThat(result2).isTrue();
        then(playerRepository)
                .should(times(1))
                .existsByBrawlStarsTag("player1");
        then(unknownPlayerRepository)
                .should(times(1))
                .existsByBrawlStarsTag("player1");
    }
}