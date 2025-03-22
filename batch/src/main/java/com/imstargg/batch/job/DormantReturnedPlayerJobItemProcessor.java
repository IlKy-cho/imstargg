package com.imstargg.batch.job;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import static com.imstargg.storage.db.core.player.QPlayerCollectionEntity.playerCollectionEntity;


public class DormantReturnedPlayerJobItemProcessor
        implements ItemProcessor<BattleCollectionEntity, List<PlayerCollectionEntity>> {

    private static final Logger log = LoggerFactory.getLogger(DormantReturnedPlayerJobItemProcessor.class);

    private final EntityManagerFactory emf;
    private final ConcurrentSkipListSet<String> tagSet;

    public DormantReturnedPlayerJobItemProcessor(EntityManagerFactory emf) {
        this.emf = emf;
        this.tagSet = new ConcurrentSkipListSet<>();
    }

    @Override
    public List<PlayerCollectionEntity> process(BattleCollectionEntity item) throws Exception {

        List<PlayerCollectionEntity> dormantReturnedPlayers = JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(playerCollectionEntity)
                .where(
                        playerCollectionEntity.brawlStarsTag.in(
                                item.getAllPlayers()
                                        .stream()
                                        .map(BattleCollectionEntityTeamPlayer::getBrawlStarsTag)
                                        .filter(tagSet::add)
                                        .toList()
                        ),
                        playerCollectionEntity.status.eq(PlayerStatus.DORMANT)
                ).fetch();

        dormantReturnedPlayers.forEach(PlayerCollectionEntity::dormantReturned);

        if (!dormantReturnedPlayers.isEmpty()) {
            log.debug("총 {}명의 플레이어가 휴면해제 되었습니다. tags={}",
                    dormantReturnedPlayers.size(),
                    dormantReturnedPlayers.stream().map(PlayerCollectionEntity::getBrawlStarsTag).toList()
            );
        }

        return dormantReturnedPlayers;
    }
}
