package com.imstargg.core.domain;

import com.imstargg.core.domain.brawlstars.BattleEventRepository;
import com.imstargg.core.domain.brawlstars.BrawlerRepository;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.Language;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.BattleEntity;
import com.imstargg.storage.db.core.BattleJpaRepository;
import com.imstargg.storage.db.core.BattlePlayerEntity;
import com.imstargg.storage.db.core.BattlePlayerJpaRepository;
import com.imstargg.storage.db.core.PlayerEntity;
import com.imstargg.storage.db.core.PlayerJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Component
public class BattleRepository {

    private final PlayerJpaRepository playerJpaRepository;
    private final BattleJpaRepository battleJpaRepository;
    private final BattlePlayerJpaRepository battlePlayerJpaRepository;
    private final BattleEventRepository battleEventRepository;
    private final BrawlerRepository brawlerRepository;

    public BattleRepository(
            PlayerJpaRepository playerJpaRepository,
            BattleJpaRepository battleJpaRepository,
            BattlePlayerJpaRepository battlePlayerJpaRepository,
            BattleEventRepository battleEventRepository,
            BrawlerRepository brawlerRepository
    ) {
        this.playerJpaRepository = playerJpaRepository;
        this.battleJpaRepository = battleJpaRepository;
        this.battlePlayerJpaRepository = battlePlayerJpaRepository;
        this.battleEventRepository = battleEventRepository;
        this.brawlerRepository = brawlerRepository;
    }

    public List<PlayerBattle> find(Player player, int page) {
        PlayerEntity playerEntity = playerJpaRepository.findByBrawlStarsTagAndDeletedFalse(player.tag().value())
                .orElseThrow(() -> new CoreException("Player not found: " + player.tag()));
        List<BattleEntity> battleEntities = battleJpaRepository.findAllByPlayerPlayerIdAndDeletedFalseOrderByBattleTimeDesc(
                playerEntity.getId(), PageRequest.of(page, 20));
        Map<Long, List<BattlePlayerEntity>> idToPlayerEntities = battlePlayerJpaRepository
                .findAllByBattleIdIn(battleEntities.stream().map(BattleEntity::getId).toList())
                .stream()
                .collect(groupingBy(BattlePlayerEntity::getBattleId));

        return battleEntities.stream()
                .map(battleEntity -> mapBattle(battleEntity, idToPlayerEntities.get(battleEntity.getId())))
                .toList();
    }

    private PlayerBattle mapBattle(BattleEntity battleEntity, List<BattlePlayerEntity> battlePlayerEntities) {
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
                mapBattleTeams(battlePlayerEntities)
        );
    }

    private List<List<BattlePlayer>> mapBattleTeams(List<BattlePlayerEntity> battlePlayerEntities) {
        List<List<BattlePlayer>> battlePlayers = new ArrayList<>();
        battlePlayerEntities.stream()
                .sorted((o1, o2) -> {
                    if (o1.getTeamIdx() != o2.getTeamIdx()) {
                        return Integer.compare(o1.getTeamIdx(), o2.getTeamIdx());
                    } else {
                        return Integer.compare(o1.getPlayerIdx(), o2.getPlayerIdx());
                    }
                })
                .forEach(entity -> {
                    if (entity.getPlayerIdx() == 0) {
                        battlePlayers.add(new ArrayList<>());
                    }
                    battlePlayers.getLast().add(new BattlePlayer(
                            new BrawlStarsTag(entity.getBrawlStarsTag()),
                            entity.getName(),
                            brawlerRepository
                                    .find(new BrawlStarsId(entity.getBrawler().getBrawlStarsId()), Language.KOREAN)
                                    .orElse(null),
                            entity.getBrawler().getPower(),
                            entity.getBrawler().getTrophies(),
                            entity.getBrawler().getTrophyChange()
                    ));
                });

        return battlePlayers.stream()
                .map(Collections::unmodifiableList)
                .toList();
    }
}
