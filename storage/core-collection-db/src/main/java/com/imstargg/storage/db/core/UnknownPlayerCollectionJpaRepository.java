package com.imstargg.storage.db.core;

import com.imstargg.core.enums.UnknownPlayerStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UnknownPlayerCollectionJpaRepository extends JpaRepository<UnknownPlayerCollectionEntity, Long> {

    boolean existsByBrawlStarsTag(String brawlStarsTag);

    List<UnknownPlayerCollectionEntity> findAllByStatusInAndDeletedFalseOrderByCreatedAtAsc(
            Collection<UnknownPlayerStatus> statuses, Limit limit);
}
