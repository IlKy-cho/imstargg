package com.imstargg.batch.job;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import static com.imstargg.storage.db.core.QPlayerCollectionEntity.playerCollectionEntity;

public class DormantReturnedPlayerJobItemProcessor
        implements ItemProcessor<BattleCollectionEntity, List<PlayerCollectionEntity>> {

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

        return dormantReturnedPlayers;
    }
}
