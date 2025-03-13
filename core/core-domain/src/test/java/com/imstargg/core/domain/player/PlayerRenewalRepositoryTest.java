package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.PlayerRenewalStatus;
import com.imstargg.storage.db.core.PlayerRenewalEntity;
import com.imstargg.storage.db.core.PlayerRenewalJpaRepository;
import com.imstargg.storage.db.core.test.AbstractDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerRenewalRepositoryTest extends AbstractDataJpaTest {

    private PlayerRenewalRepository playerRenewalRepository;

    @Autowired
    private PlayerRenewalJpaRepository playerRenewalJpaRepository;

    private Clock clock;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        playerRenewalRepository = new PlayerRenewalRepository(
                clock,
                playerRenewalJpaRepository
        );
    }

    @Test
    void get_존재하는_태그_조회() {
        // given
        String tag = "#123";
        PlayerRenewalEntity entity = new PlayerRenewalEntity(tag, OffsetDateTime.now(clock));
        playerRenewalJpaRepository.save(entity);

        // when
        PlayerRenewal playerRenewal = playerRenewalRepository.get(new BrawlStarsTag(tag));

        // then
        assertThat(playerRenewal.tag().value()).isEqualTo(tag);
        assertThat(playerRenewal.status()).isEqualTo(PlayerRenewalStatus.NEW);
    }

    @Test
    void get_존재하지_않는_태그_조회() {
        // given
        String tag = "#123";

        // when
        PlayerRenewal playerRenewal = playerRenewalRepository.get(new BrawlStarsTag(tag));

        // then
        assertThat(playerRenewal.tag().value()).isEqualTo(tag);
        assertThat(playerRenewal.status()).isEqualTo(PlayerRenewalStatus.NEW);
    }

    @Test
    void find_존재하는_태그_조회() {
        // given
        String tag = "#123";
        PlayerRenewalEntity entity = new PlayerRenewalEntity(tag, OffsetDateTime.now(clock));
        playerRenewalJpaRepository.save(entity);

        // when
        Optional<PlayerRenewal> playerRenewal = playerRenewalRepository.find(new BrawlStarsTag(tag));

        // then
        assertThat(playerRenewal).isPresent();
        assertThat(playerRenewal.get().tag().value()).isEqualTo(tag);
        assertThat(playerRenewal.get().status()).isEqualTo(PlayerRenewalStatus.NEW);
    }

    @Test
    void find_존재하지_않는_태그_조회() {
        // given
        String tag = "#123";

        // when
        Optional<PlayerRenewal> playerRenewal = playerRenewalRepository.find(new BrawlStarsTag(tag));

        // then
        assertThat(playerRenewal).isEmpty();
    }

    @Test
    void pending_성공() {
        // given
        String tag = "#123";
        PlayerRenewalEntity entity = new PlayerRenewalEntity(tag, OffsetDateTime.now(clock));
        playerRenewalJpaRepository.save(entity);
        PlayerRenewal playerRenewal = new PlayerRenewal(
                new BrawlStarsTag(tag),
                PlayerRenewalStatus.COMPLETE,
                OffsetDateTime.now(clock)
        );

        // when
        boolean result = playerRenewalRepository.pending(playerRenewal);

        // then
        assertThat(result).isTrue();
        PlayerRenewalEntity updatedEntity = playerRenewalJpaRepository.findByBrawlStarsTag(tag).get();
        assertThat(updatedEntity.getStatus()).isEqualTo(PlayerRenewalStatus.PENDING);
    }

    @Test
    void countRenewing_갱신중인_플레이어_수_조회() {
        // given
        List<PlayerRenewalEntity> entities = List.of(
                new PlayerRenewalEntity("#1", OffsetDateTime.now(clock)), // NEW
                new PlayerRenewalEntity("#2", OffsetDateTime.now(clock))  // PENDING
        );
        entities.get(1).pending(OffsetDateTime.now(clock));
        playerRenewalJpaRepository.saveAll(entities);

        // when
        int count = playerRenewalRepository.countRenewing();

        // then
        assertThat(count).isEqualTo(1);
    }
}