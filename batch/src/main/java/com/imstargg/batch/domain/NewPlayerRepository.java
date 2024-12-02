package com.imstargg.batch.domain;

import com.imstargg.core.enums.UnknownPlayerStatus;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionJpaRepository;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewPlayerRepository {

    private final UnknownPlayerCollectionJpaRepository unknownPlayerRepository;

    public NewPlayerRepository(UnknownPlayerCollectionJpaRepository unknownPlayerRepository) {
        this.unknownPlayerRepository = unknownPlayerRepository;
    }


    public List<UnknownPlayerCollectionEntity> find(int size) {
        return unknownPlayerRepository
                .findAllByStatusInAndDeletedFalseOrderByCreatedAtAsc(
                        List.of(UnknownPlayerStatus.ADMIN_NEW, UnknownPlayerStatus.UPDATE_NEW),
                        Limit.of(size)
                );
    }
}
