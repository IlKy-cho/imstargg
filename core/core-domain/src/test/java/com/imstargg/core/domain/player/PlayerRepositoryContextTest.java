package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.domain.test.ContextTest;
import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.player.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import com.imstargg.storage.db.core.player.UnknownPlayerEntity;
import com.imstargg.storage.db.core.test.CleanUp;
import com.imstargg.storage.db.core.test.EntityAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(EntityAppender.class)
class PlayerRepositoryContextTest extends ContextTest {

    @Autowired
    private EntityAppender entityAppender;

    @Autowired
    private CleanUp cleanUp;

    @Autowired
    private PlayerRepository playerRepository;

    @AfterEach
    void tearDown() {
        cleanUp.all();
    }

    @Test
    void 플레이어를_조회한다() {
        // given
        var player = new PlayerCollectionEntity(
                "tag",
                "name",
                "nameColor",
                1L,
                1,
                2,
                3,
                4,
                true,
                5,
                6,
                7,
                8,
                9,
                "clubTag"
        );
        entityAppender.append(player);

        // when
        var result = playerRepository.findByTag(new BrawlStarsTag("tag")).get();

        // then
        assertThat(result.tag()).isEqualTo(new BrawlStarsTag("tag"));
        assertThat(result.name()).isEqualTo("name");
        assertThat(result.nameColor()).isEqualTo("nameColor");
        assertThat(result.iconId()).isEqualTo(new BrawlStarsId(1L));
        assertThat(result.trophies()).isEqualTo(1);
        assertThat(result.highestTrophies()).isEqualTo(2);
        assertThat(result.clubTag()).isEqualTo(new BrawlStarsTag("clubTag"));
        assertThat(result.soloRankTier()).isNull();
    }

    @Test
    void 모르는_플레이어를_조회한다() {
        // given
        var unknownPlayerEntity = new UnknownPlayerEntity(
                "tag"
        );
        entityAppender.append(unknownPlayerEntity);

        // when
        var result = playerRepository.getUnknown(new BrawlStarsTag("tag"));

        // then
        assertThat(result.tag()).isEqualTo(new BrawlStarsTag("tag"));
        assertThat(result.notFoundCount()).isZero();
    }

    @Test
    void 모르는_플레이어_조회시_존재하지_않을경우_생성한다() {
        // given
        // when
        var result = playerRepository.getUnknown(new BrawlStarsTag("tag"));

        // then
        assertThat(result.tag()).isEqualTo(new BrawlStarsTag("tag"));
        assertThat(result.notFoundCount()).isZero();
    }

    @Test
    void 플레이어_브롤러를_조회한다() {
        // given
        var playerEntity = new PlayerCollectionEntity(
                "tag",
                "name",
                "nameColor",
                1L,
                1,
                2,
                3,
                4,
                true,
                5,
                6,
                7,
                8,
                9,
                "clubTag"
        );
        var playerBrawlerEntity = new PlayerBrawlerCollectionEntity(
                playerEntity,
                1L,
                2,
                3,
                4,
                5,
                List.of(),
                List.of(7L),
                List.of(8L, 9L)
        );
        entityAppender.append(playerEntity);
        entityAppender.append(playerBrawlerEntity);

        // when
        List<PlayerBrawler> result = playerRepository.findBrawlers(
                playerRepository.findByTag(new BrawlStarsTag("tag")).get());

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(new BrawlStarsId(1L));
        assertThat(result.get(0).power()).isEqualTo(2);
        assertThat(result.get(0).rank()).isEqualTo(3);
        assertThat(result.get(0).trophies()).isEqualTo(4);
        assertThat(result.get(0).highestTrophies()).isEqualTo(5);
        assertThat(result.get(0).gearIds()).isEmpty();
        assertThat(result.get(0).starPowerIds()).containsExactly(new BrawlStarsId(7L));
        assertThat(result.get(0).gadgetIds()).containsExactlyInAnyOrder(new BrawlStarsId(8L), new BrawlStarsId(9L));
    }

    @Test
    void 플레이어_브롤러_조회시_플레이어가_존재하지_않을경우_예외가_발생한다() {
        // given
        // given
        var playerEntity = new PlayerCollectionEntity(
                "tag",
                "name",
                "nameColor",
                1L,
                1,
                2,
                3,
                4,
                true,
                5,
                6,
                7,
                8,
                9,
                "clubTag"
        );
        var playerBrawlerEntity = new PlayerBrawlerCollectionEntity(
                playerEntity,
                1L,
                2,
                3,
                4,
                5,
                List.of(),
                List.of(7L),
                List.of(8L, 9L)
        );
        entityAppender.append(playerEntity);
        entityAppender.append(playerBrawlerEntity);
        // when
        // then
        Player player = new PlayerFixture()
                .tag(new BrawlStarsTag(playerEntity.getBrawlStarsTag() + "-other"))
                .build();
        assertThatThrownBy(() -> playerRepository.findBrawlers(player))
                .isInstanceOf(CoreException.class)
                .extracting("errorType")
                .isEqualTo(CoreErrorType.DEFAULT_ERROR);
    }
}
