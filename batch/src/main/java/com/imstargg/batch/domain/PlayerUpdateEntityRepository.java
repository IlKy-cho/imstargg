package com.imstargg.batch.domain;

import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionJpaRepository;
import com.imstargg.storage.db.core.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.PlayerBrawlerCollectionJpaRepository;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Limit;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PlayerUpdateEntityRepository {

    private final PlayerCollectionJpaRepository playerCollectionJpaRepository;
    private final PlayerBrawlerCollectionJpaRepository playerBrawlerCollectionJpaRepository;
    private final BattleCollectionJpaRepository battleCollectionJpaRepository;

    public PlayerUpdateEntityRepository(
            PlayerCollectionJpaRepository playerCollectionJpaRepository,
            PlayerBrawlerCollectionJpaRepository playerBrawlerCollectionJpaRepository,
            BattleCollectionJpaRepository battleCollectionJpaRepository) {
        this.playerCollectionJpaRepository = playerCollectionJpaRepository;
        this.playerBrawlerCollectionJpaRepository = playerBrawlerCollectionJpaRepository;
        this.battleCollectionJpaRepository = battleCollectionJpaRepository;
    }

    @Transactional
    @Retryable(retryFor = OptimisticLockingFailureException.class)
    public List<PlayerToUpdateEntity> find(int size) {
        List<PlayerCollectionEntity> players = playerCollectionJpaRepository
                .findAllWithOptimisticLockByDeletedFalseAndStatusInOrderByUpdateWeight(
                        List.of(PlayerStatus.UPDATED, PlayerStatus.UPDATING),
                        Limit.of(size)
                );
        players.forEach(player -> player.setStatus(PlayerStatus.UPDATING));
        List<Long> playerIds = getPlayerIds(players);
        Map<Long, List<PlayerBrawlerCollectionEntity>> playerIdToBrawlers = getPlayerIdToBrawlers(playerIds);
        Map<Long, BattleCollectionEntity> playerIdToBattle = getPlayerIdToBattle(playerIds);

        return players.stream()
                .map(player -> {
                    List<PlayerBrawlerCollectionEntity> brawlers = playerIdToBrawlers
                            .getOrDefault(player.getId(), List.of());
                    Optional<BattleCollectionEntity> battle = Optional.ofNullable(
                            playerIdToBattle.get(player.getId()));
                    return new PlayerToUpdateEntity(player, brawlers, battle);
                })
                .toList();
    }

    private static List<Long> getPlayerIds(List<PlayerCollectionEntity> players) {
        return players.stream()
                .map(PlayerCollectionEntity::getId)
                .toList();
    }

    private Map<Long, List<PlayerBrawlerCollectionEntity>> getPlayerIdToBrawlers(List<Long> playerIds) {
        return playerBrawlerCollectionJpaRepository
                .findAllByPlayerIdIn(playerIds)
                .stream()
                .collect(Collectors.groupingBy(brawler -> brawler.getPlayer().getId()));
    }

    private Map<Long, BattleCollectionEntity> getPlayerIdToBattle(List<Long> playerIds) {
        return battleCollectionJpaRepository
                .findAllLastBattleByPlayerIdIn(playerIds)
                .stream()
                .collect(Collectors.toMap(battle -> battle.getPlayer().getPlayer().getId(), battle -> battle));
    }
}
