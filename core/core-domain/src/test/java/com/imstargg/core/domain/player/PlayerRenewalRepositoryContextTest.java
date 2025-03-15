package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.PlayerRenewalStatus;
import com.imstargg.storage.db.core.PlayerRenewalEntity;
import com.imstargg.storage.db.core.PlayerRenewalJpaRepository;
import com.imstargg.storage.db.core.test.CleanUp;
import com.imstargg.test.java.MultiThreadUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("context")
@SpringBootTest
class PlayerRenewalRepositoryContextTest {

    @Autowired
    private CleanUp cleanUp;

    @Autowired
    private PlayerRenewalRepository playerRenewalRepository;

    @Autowired
    private PlayerRenewalJpaRepository playerRenewalJpaRepository;

    @AfterEach
    void tearDown() {
        cleanUp.all();
    }

    @Test
    void get_존재하는_태그_조회() {
        // given
        String tag = "#123";
        PlayerRenewalEntity entity = new PlayerRenewalEntity(tag);
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
        PlayerRenewalEntity entity = new PlayerRenewalEntity(tag);
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
    void pending_상태로_업데이트한다() {
        // given
        String tag = "#123";
        PlayerRenewalEntity entity = new PlayerRenewalEntity(tag);
        playerRenewalJpaRepository.save(entity);
        PlayerRenewal playerRenewal = new PlayerRenewal(
                new BrawlStarsTag(tag),
                PlayerRenewalStatus.COMPLETE,
                entity.getUpdatedAt()
        );

        // when
        boolean result = playerRenewalRepository.pending(playerRenewal);

        // then
        assertThat(result).isTrue();
        PlayerRenewalEntity updatedEntity = playerRenewalJpaRepository.findByBrawlStarsTag(tag).get();
        assertThat(updatedEntity.getStatus()).isEqualTo(PlayerRenewalStatus.PENDING);
    }

    @Test
    void 동시에_pending_업데이트할_경우_1번만_성공한다() throws Exception {
        // given
        String tag = "#123";
        PlayerRenewalEntity entity = new PlayerRenewalEntity(tag);
        playerRenewalJpaRepository.save(entity);
        PlayerRenewal playerRenewal = new PlayerRenewal(
                new BrawlStarsTag(tag),
                PlayerRenewalStatus.COMPLETE,
                entity.getUpdatedAt()
        );

        // when
        List<Boolean> result = MultiThreadUtil.execute(() -> playerRenewalRepository.pending(playerRenewal), 10);

        // then
        assertThat(result).containsExactlyInAnyOrder(true, false, false, false, false, false, false, false, false, false);
    }

    @Test
    void countRenewing_갱신중인_플레이어_수_조회() {
        // given
        List<PlayerRenewalEntity> entities = List.of(
                new PlayerRenewalEntity("#1"), // NEW
                new PlayerRenewalEntity("#2")  // PENDING
        );
        entities.get(1).pending();
        playerRenewalJpaRepository.saveAll(entities);

        // when
        int count = playerRenewalRepository.countRenewing();

        // then
        assertThat(count).isEqualTo(1);
    }
}