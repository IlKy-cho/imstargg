package com.imstargg.admin.domain;

import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.MessageCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BattleMapCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BattleMapCollectionJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BattleMapService {

    private final BattleMapCollectionJpaRepository battleMapRepository;
    private final MessageCollectionJpaRepository messageRepository;

    public BattleMapService(
            BattleMapCollectionJpaRepository battleMapRepository,
            MessageCollectionJpaRepository messageRepository
    ) {
        this.battleMapRepository = battleMapRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public void register(NewBattleMap newBattleMap) {
        newBattleMap.names().validate();
        BattleMapCollectionEntity battleMap = battleMapRepository.save(new BattleMapCollectionEntity());

        newBattleMap.names().messages().forEach((language, name) -> messageRepository.save(
                new MessageCollectionEntity(battleMap.getNameMessageCode(), language.getCode(), name)));
    }
}
