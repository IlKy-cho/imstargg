package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.PlayerCollectionEntityFixture;
import com.imstargg.storage.db.core.test.AbstractDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(PlayerTagFilter.class)
class PlayerTagFilterContextTest extends AbstractDataJpaTest {

    private PlayerTagFilter playerTagFilter;

    @BeforeEach
    void setUp() {
        playerTagFilter = new PlayerTagFilter(emf);
    }

    @Test
    void DB에_존재하는_플레이어는_필터링한다() {
        // given
        entityAppender.append(
                new PlayerCollectionEntityFixture()
                        .brawlStarsTag("#1")
                        .build()
        );

        // when
        List<String> result = playerTagFilter.filter(List.of("#1", "#2", "#3"));
        assertThat(result).hasSize(2)
                .containsExactlyInAnyOrder("#2", "#3");
    }

    @Test
    void 한번_필터링되지_않은_태그는_이후_필터링된다() {
        // given
        entityAppender.append(
                new PlayerCollectionEntityFixture()
                        .brawlStarsTag("#1")
                        .build()
        );

        // when
        List<String> result = playerTagFilter.filter(List.of("#2", "#3"));
        assertThat(result).hasSize(2)
                .containsExactlyInAnyOrder("#2", "#3");

        // when
        result = playerTagFilter.filter(List.of("#1", "#2", "#3"));
        assertThat(result).isEmpty();
    }
}