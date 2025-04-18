package com.imstargg.worker.domain;

import com.imstargg.storage.db.core.player.PlayerRenewalCollectionEntity;
import com.imstargg.storage.db.core.player.PlayerRenewalEntity;
import com.imstargg.storage.db.core.test.AbstractDataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(PlayerRenewalReader.class)
class PlayerRenewalReaderTest extends AbstractDataJpaTest {

    @Autowired
    private PlayerRenewalReader playerRenewalReader;


    @Test
    void 플레이어_갱신정보를_조회한다() {
        // given
        String brawlStarsTag = "#12345";
        em.persist(new PlayerRenewalEntity(brawlStarsTag));
        em.flush();
        em.clear();

        // when
        PlayerRenewalCollectionEntity result = playerRenewalReader.get(brawlStarsTag);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBrawlStarsTag()).isEqualTo(brawlStarsTag);
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    void 플레이어_갱신정보가_존재하지_않으면_예외가_발생한다() {
        // given
        String brawlStarsTag = "#12345";

        // when & then
        assertThatThrownBy(() -> playerRenewalReader.get(brawlStarsTag))
                .isInstanceOf(IllegalStateException.class);
    }
}