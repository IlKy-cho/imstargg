package com.imstargg.admin.domain;

import com.imstargg.admin.support.error.AdminErrorKind;
import com.imstargg.admin.support.error.AdminException;
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
        if (battleMapRepository.findByCode(newBattleMap.code()).isPresent()) {
            throw new AdminException(AdminErrorKind.DUPLICATED,
                    "이미 등록된 배틀맵입니다. code: " + newBattleMap.code());
        }
        BattleMapCollectionEntity battleMap = battleMapRepository.save(new BattleMapCollectionEntity(
                newBattleMap.code()
        ));

        newBattleMap.names().messages().forEach((language, name) -> messageRepository.save(
                new MessageCollectionEntity(battleMap.getNameMessageCode(), language.getCode(), name)));
    }
}
