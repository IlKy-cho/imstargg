package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.domain.Slice;
import com.imstargg.core.domain.brawlstars.BattleEvent;
import com.imstargg.core.domain.brawlstars.BattleEventMap;
import com.imstargg.core.domain.brawlstars.BattleEventRepositoryWithCache;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleMode;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.SoloRankTier;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.BattleEntity;
import com.imstargg.storage.db.core.BattleEntityTeamPlayer;
import com.imstargg.storage.db.core.BattleJpaRepository;
import com.imstargg.storage.db.core.MessageCodes;
import com.imstargg.storage.db.core.PlayerEntity;
import com.imstargg.storage.db.core.PlayerJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Component
public class BattleRepository {

    private final PlayerJpaRepository playerJpaRepository;
    private final BattleJpaRepository battleJpaRepository;
    private final BattleEventRepositoryWithCache battleEventRepository;

    public BattleRepository(
            PlayerJpaRepository playerJpaRepository,
            BattleJpaRepository battleJpaRepository,
            BattleEventRepositoryWithCache battleEventRepository
    ) {
        this.playerJpaRepository = playerJpaRepository;
        this.battleJpaRepository = battleJpaRepository;
        this.battleEventRepository = battleEventRepository;
    }

    public Slice<PlayerBattle> find(Player player, int page, int size) {
        PlayerEntity playerEntity = playerJpaRepository
                .findByBrawlStarsTagAndDeletedFalse(player.tag().value())
                .orElseThrow(() -> new CoreException("Player not found. tag=" + player.tag()));
        var battleEntitySlice = battleJpaRepository
                .findAllByPlayerPlayerIdAndDeletedFalseOrderByBattleTimeDesc(
                        playerEntity.getId(), PageRequest.of(page - 1, size));

        Map<BrawlStarsId, BattleEvent> eventIdToEvent = findEvents(battleEntitySlice.getContent())
                .stream().collect(toMap(BattleEvent::id, Function.identity()));

        return new Slice<>(
                battleEntitySlice.map(battle -> mapBattle(battle, eventIdToEvent)).toList(),
                battleEntitySlice.hasNext()
        );
    }

    private List<BattleEvent> findEvents(List<BattleEntity> battleEntities) {
        List<BrawlStarsId> eventIds = battleEntities.stream()
                .map(battle -> battle.getEvent().getBrawlStarsId())
                .filter(Objects::nonNull)
                .filter(eventBrawlStarsId -> eventBrawlStarsId > 0)
                .distinct()
                .map(BrawlStarsId::new)
                .toList();
        return battleEventRepository.findAllEvents(eventIds);
    }

    private PlayerBattle mapBattle(
            BattleEntity battleEntity, Map<BrawlStarsId, BattleEvent> eventIdToEvent) {
        BattleType battleType = BattleType.find(battleEntity.getType());
        return new PlayerBattle(
                battleEntity.getBattleTime(),
                mapBattleEvent(battleEntity, eventIdToEvent),
                BattleMode.find(battleEntity.getMode()),
                battleType,
                battleEntity.getResult() != null ? BattleResult.map(battleEntity.getResult()) : null,
                battleEntity.getDuration(),
                battleEntity.getPlayer().getRank(),
                battleEntity.getPlayer().getTrophyChange(),
                battleEntity.getStarPlayerBrawlStarsTag() != null
                        ? new BrawlStarsTag(battleEntity.getStarPlayerBrawlStarsTag()) : null,
                mapTeams(battleType, battleEntity.getTeams())
        );
    }

    private PlayerBattleEvent mapBattleEvent(
            BattleEntity battleEntity, Map<BrawlStarsId, BattleEvent> eventIdToEvent
    ) {
        return Optional.ofNullable(battleEntity.getEvent().getBrawlStarsId())
                .filter(id -> id > 0)
                .map(BrawlStarsId::new)
                .map(eventIdToEvent::get)
                .map(event ->
                        new PlayerBattleEvent(
                                event.id(),
                                event.mode(),
                                event.map()
                        )
                ).orElseGet(() ->
                        new PlayerBattleEvent(
                                new BrawlStarsId(battleEntity.getEvent().getBrawlStarsId()),
                                BattleEventMode.find(battleEntity.getEvent().getMode()),
                                new BattleEventMap(
                                        Optional.ofNullable(battleEntity.getEvent().getMap())
                                                .map(battleMap -> MessageCollection.newDefault(
                                                        MessageCodes.BATTLE_MAP_NAME.code(battleMap), battleMap)
                                                ).orElse(null),
                                        null
                                )
                        )
                );
    }

    private List<List<BattlePlayer>> mapTeams(
            BattleType battleType, List<List<BattleEntityTeamPlayer>> teams) {
        return teams.stream()
                .map(team -> team.stream()
                        .map(player -> mapPlayer(battleType, player)).toList()
                ).toList();
    }

    private BattlePlayer mapPlayer(BattleType battleType, BattleEntityTeamPlayer player) {
        return new BattlePlayer(
                new BrawlStarsTag(player.getBrawlStarsTag()),
                player.getName(),
                battleType == BattleType.SOLO_RANKED ? SoloRankTier.of(player.getBrawler().getTrophies()) : null,
                mapBattlePlayerBrawler(battleType, player)
        );
    }

    private BattlePlayerBrawler mapBattlePlayerBrawler(BattleType battleType, BattleEntityTeamPlayer player) {
        boolean isSoloRanked = BattleType.SOLO_RANKED.equals(battleType);
        return new BattlePlayerBrawler(
                new BrawlStarsId(player.getBrawler().getBrawlStarsId()),
                player.getBrawler().getPower(),
                isSoloRanked ? null : player.getBrawler().getTrophies(),
                isSoloRanked ? null : player.getBrawler().getTrophyChange()
        );
    }
}
