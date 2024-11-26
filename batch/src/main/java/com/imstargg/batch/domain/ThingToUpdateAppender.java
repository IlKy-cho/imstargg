package com.imstargg.batch.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imstargg.core.enums.UpdateType;
import com.imstargg.storage.db.core.ThingToUpdateCollectionEntity;
import com.imstargg.storage.db.core.ThingToUpdateCollectionJpaRepository;
import com.imstargg.support.alert.AlertCommand;
import com.imstargg.support.alert.AlertManager;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.UncheckedIOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ThingToUpdateAppender {

    private final ThingToUpdateCollectionJpaRepository thingToUpdateJpaRepository;
    private final AlertManager alertManager;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<Long, CopyOnWriteArrayList<ThingToUpdateCollectionEntity>> cache;


    public ThingToUpdateAppender(
            ThingToUpdateCollectionJpaRepository thingToUpdateJpaRepository,
            AlertManager alertManager
    ) {
        this.thingToUpdateJpaRepository = thingToUpdateJpaRepository;
        this.alertManager = alertManager;
        this.objectMapper = new ObjectMapper();
        this.cache = new ConcurrentHashMap<>();
    }

    @PostConstruct
    void refreshCache() {
        cache.clear();
        thingToUpdateJpaRepository.findAllByDeletedFalse().forEach(thingToUpdate -> {
            cache.computeIfAbsent(thingToUpdate.getBrawlStarsId(),
                    k -> new CopyOnWriteArrayList<>()).add(thingToUpdate);
        });
    }

    public void append(long brawlStarsId, UpdateType type, Object data) {
        ThingToUpdateCollectionEntity thingToUpdate = new ThingToUpdateCollectionEntity(
                type,
                brawlStarsId,
                mapToJson(data)
        );
        if (exists(thingToUpdate)) {
            return;
        }
        refreshCache();
        if (exists(thingToUpdate)) {
            return;
        }

        ThingToUpdateCollectionEntity saved = thingToUpdateJpaRepository.save(thingToUpdate);
        cache.computeIfAbsent(saved.getBrawlStarsId(), k -> new CopyOnWriteArrayList<>()).add(saved);
        alertManager.alert(AlertCommand.builder()
                .title("업데이트할 항목이 생겼습니다.")
                .content(String.format(
                        "type: %s, brawlStarsId: %d\n%s",
                        thingToUpdate.getType(), thingToUpdate.getBrawlStarsId(), thingToUpdate.getData()
                ))
                .build());
    }

    private String mapToJson(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    private boolean exists(ThingToUpdateCollectionEntity thingToUpdate) {
        CopyOnWriteArrayList<ThingToUpdateCollectionEntity> cached = cache.get(thingToUpdate.getBrawlStarsId());
        return cached != null && cached.stream().anyMatch(
                t -> t.getData().equals(thingToUpdate.getData()));
    }
}