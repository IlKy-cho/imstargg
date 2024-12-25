package com.imstargg.core.domain;

import com.imstargg.core.domain.brawlstars.BattleEventRepository;
import com.imstargg.core.domain.brawlstars.BrawlerRepositoryWithCache;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.Language;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.BattleEntity;
import com.imstargg.storage.db.core.BattleEntityTeamPlayer;
import com.imstargg.storage.db.core.BattleJpaRepository;
import com.imstargg.storage.db.core.PlayerEntity;
import com.imstargg.storage.db.core.PlayerJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BattleRepository {

    private static final int PAGE_SIZE = 25;

    private final PlayerJpaRepository playerJpaRepository;
    private final BattleJpaRepository battleJpaRepository;
    private final BattleEventRepository battleEventRepository;
    private final BrawlerRepositoryWithCache brawlerRepository;

    public BattleRepository(
            PlayerJpaRepository playerJpaRepository,
            BattleJpaRepository battleJpaRepository,
            BattleEventRepository battleEventRepository,
            BrawlerRepositoryWithCache brawlerRepository
    ) {
        this.playerJpaRepository = playerJpaRepository;
        this.battleJpaRepository = battleJpaRepository;
        this.battleEventRepository = battleEventRepository;
        this.brawlerRepository = brawlerRepository;
    }

    public List<PlayerBattle> find(Player player, int page) {
        PlayerEntity playerEntity = playerJpaRepository.findByBrawlStarsTagAndDeletedFalse(player.tag().value())
                .orElseThrow(() -> new CoreException("Player not found: " + player.tag()));
        List<BattleEntity> battleEntities = battleJpaRepository.findAllByPlayerPlayerIdAndDeletedFalseOrderByBattleTimeDesc(
                playerEntity.getId(), PageRequest.of(page, PAGE_SIZE));

        return battleEntities.stream()
                .map(this::mapBattle)
                .toList();
    }

    private PlayerBattle mapBattle(BattleEntity battleEntity) {
        return new PlayerBattle(
                battleEntity.getBattleTime(),
                battleEventRepository.find(
                                battleEntity.getEvent().getEventBrawlStarsId() != null
                                        ? new BrawlStarsId(battleEntity.getEvent().getEventBrawlStarsId()) : null,
                                Language.KOREAN)
                        .orElse(null),
                BattleType.find(battleEntity.getType()),
                battleEntity.getResult() != null ? BattleResult.map(battleEntity.getResult()) : null,
                battleEntity.getDuration(),
                battleEntity.getPlayer().getRank(),
                battleEntity.getPlayer().getTrophyChange(),
                new BrawlStarsTag(battleEntity.getStarPlayerBrawlStarsTag()),
                mapTeams(battleEntity.getTeams())
        );
    }

    private List<List<BattlePlayer>> mapTeams(List<List<BattleEntityTeamPlayer>> teams) {
        return teams.stream()
                .map(team -> team.stream()
                        .map(this::mapPlayer).toList()
                ).toList();
    }

    private BattlePlayer mapPlayer(BattleEntityTeamPlayer player) {
        return new BattlePlayer(
                new BrawlStarsTag(player.getBrawlStarsTag()),
                player.getName(),
                new BattlePlayerBrawler(
                        brawlerRepository.find(
                                new BrawlStarsId(player.getBrawler().getBrawlStarsId()),
                                Language.KOREAN
                        ).orElse(null),
                        player.getBrawler().getPower(),
                        player.getBrawler().getTrophies(),
                        player.getBrawler().getTrophyChange()
                )
        );
    }
}
