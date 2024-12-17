package com.imstargg.admin.domain;

import com.imstargg.core.enums.BrawlStarsImageType;
import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.MessageCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BattleMapCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BattleMapCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionJpaRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Service
public class BattleService {

    private final BrawlStarsImageUploader brawlStarsImageUploader;
    private final BattleMapCollectionJpaRepository battleMapRepository;
    private final BattleEventCollectionJpaRepository battleEventRepository;
    private final BrawlStarsImageCollectionJpaRepository brawlStarsImageRepository;
    private final MessageCollectionJpaRepository messageRepository;

    public BattleService(
            BrawlStarsImageUploader brawlStarsImageUploader,
            BattleMapCollectionJpaRepository battleMapRepository,
            BattleEventCollectionJpaRepository battleEventRepository,
            BrawlStarsImageCollectionJpaRepository brawlStarsImageRepository,
            MessageCollectionJpaRepository messageRepository
    ) {
        this.brawlStarsImageUploader = brawlStarsImageUploader;
        this.battleMapRepository = battleMapRepository;
        this.battleEventRepository = battleEventRepository;
        this.brawlStarsImageRepository = brawlStarsImageRepository;
        this.messageRepository = messageRepository;
    }

    public List<BattleMap> getMapList() {
        List<BattleMapCollectionEntity> maps = battleMapRepository.findAll();
        Map<String, BrawlStarsImageCollectionEntity> codeToImage = brawlStarsImageRepository
                .findAllByType(BrawlStarsImageType.BATTLE_MAP).stream()
                .collect(toMap(BrawlStarsImageCollectionEntity::getCode, Function.identity()));
        Map<String, List<MessageCollectionEntity>> codeToMessages = messageRepository.findAllByCodeIn(maps.stream()
                        .map(BattleMapCollectionEntity::getNameMessageCode).toList())
                .stream()
                .collect(groupingBy(MessageCollectionEntity::getCode));
        Map<Long, List<BattleEventCollectionEntity>> mapIdToEvent = battleEventRepository.findAll().stream()
                .collect(groupingBy(BattleEventCollectionEntity::getMapId));


        return maps.stream()
                .map(map ->
                        new BattleMap(
                                map,
                                codeToMessages.get(map.getNameMessageCode()),
                                codeToImage.get(map.getCode()),
                                mapIdToEvent.getOrDefault(map.getId(), List.of())
                        )
                ).toList();
    }

    @Transactional
    public void registerMap(NewBattleMap newBattleMap) {
        newBattleMap.names().validate();
        BattleMapCollectionEntity battleMap = battleMapRepository.save(new BattleMapCollectionEntity());

        newBattleMap.names().messages().forEach((language, name) -> messageRepository.save(
                new MessageCollectionEntity(battleMap.getNameMessageCode(), language, name)));
    }

    public void uploadMapImage(String mapCode, Resource resource) {
        brawlStarsImageUploader.uploadMap(mapCode, resource);
    }
}
