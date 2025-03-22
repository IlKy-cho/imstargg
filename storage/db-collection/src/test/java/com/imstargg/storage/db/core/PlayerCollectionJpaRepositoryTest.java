package com.imstargg.storage.db.core;

import com.imstargg.storage.db.core.player.PlayerCollectionJpaRepository;
import com.imstargg.storage.db.core.test.AbstractDataJpaTest;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class PlayerCollectionJpaRepositoryTest extends AbstractDataJpaTest {

    @Autowired
    private PlayerCollectionJpaRepository playerCollectionJpaRepository;

    @Test
    void findVersionedWithBrawlersByBrawlStarsTag() {
        // given
        var player = new PlayerCollectionEntityFixture().build();
        player.updateBrawler(
                1000L,
                1,
                1,
                1,
                1,
                List.of(),
                List.of(),
                List.of()
        );
        playerCollectionJpaRepository.save(player);
        
        em.flush();
        em.clear();

        // when
        var result = playerCollectionJpaRepository
                .findVersionedWithBrawlersByBrawlStarsTag(player.getBrawlStarsTag()).get();

        // then
        assertThat(result).isNotNull();
        assertThat(Hibernate.isInitialized(result.getBrawlers())).isTrue();
        assertThat(result.getBrawlers()).hasSize(1);
        assertThat(result.getBrawlers().get(0).getPower()).isEqualTo(1);
        assertThat(result.getBrawlers().get(0).getRank()).isEqualTo(1);
        assertThat(result.getBrawlers().get(0).getTrophies()).isEqualTo(1);
        assertThat(result.getBrawlers().get(0).getHighestTrophies()).isEqualTo(1);
    }
}