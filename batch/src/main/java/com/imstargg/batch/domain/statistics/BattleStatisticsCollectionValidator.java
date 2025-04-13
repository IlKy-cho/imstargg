package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleType;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayerBrawler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class BattleStatisticsCollectionValidator {

    private static final Set<BattleType> RESULT_STATISTICS_BATTLE_TYPES = Set.of(
            BattleType.RANKED,
            BattleType.SOLO_RANKED,
            BattleType.CHALLENGE,
            BattleType.TOURNAMENT,
            BattleType.CHAMPIONSHIP_CHALLENGE
    );
    private static final Set<BattleType> RANK_STATISTICS_BATTLE_TYPES = Set.of(
            BattleType.RANKED
    );

    public boolean validateResultStatisticsCollection(BattleCollectionEntity battle) {
        return battle.getResult() != null
                && existsEventId(battle)
                && RESULT_STATISTICS_BATTLE_TYPES.contains(BattleType.find(battle.getType()))
                && !containsDuplicateBrawler(battle)
                && battle.getTeams().size() == 2;
    }

    public boolean validateRankStatisticsCollection(BattleCollectionEntity battle) {
        return battle.getPlayer().getRank() != null
                && existsEventId(battle)
                && RANK_STATISTICS_BATTLE_TYPES.contains(BattleType.find(battle.getType()));
    }

    private boolean existsEventId(BattleCollectionEntity battle) {
        return battle.getEvent().getBrawlStarsId() != null && battle.getEvent().getBrawlStarsId() > 0;
    }

    private boolean containsDuplicateBrawler(BattleCollectionEntity battle) {
        List<Long> brawlerBrawlStarsIds = battle.getTeams().stream()
                .flatMap(List::stream)
                .map(BattleCollectionEntityTeamPlayer::getBrawler)
                .map(BattleCollectionEntityTeamPlayerBrawler::getBrawlStarsId)
                .toList();
        return brawlerBrawlStarsIds.size() != (int) brawlerBrawlStarsIds.stream().distinct().count();
    }
}
