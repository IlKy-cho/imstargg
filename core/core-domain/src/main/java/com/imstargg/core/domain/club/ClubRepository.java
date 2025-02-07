package com.imstargg.core.domain.club;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.ClubType;
import com.imstargg.storage.db.core.club.ClubEntity;
import com.imstargg.storage.db.core.club.ClubJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClubRepository {

    private final ClubJpaRepository clubJpaRepository;

    public ClubRepository(ClubJpaRepository clubJpaRepository) {
        this.clubJpaRepository = clubJpaRepository;
    }

    public Optional<Club> findByTag(BrawlStarsTag tag) {
        return clubJpaRepository.findByBrawlStarsTagAndDeletedFalse(tag.value())
                .map(this::mapEntityToClub);
    }

    private Club mapEntityToClub(ClubEntity entity) {
        return new Club(
                new BrawlStarsTag(entity.getBrawlStarsTag()),
                entity.getName(),
                entity.getDescription(),
                ClubType.find(entity.getType()),
                new BrawlStarsId(entity.getBadgeBrawlStarsId()),
                entity.getRequiredTrophies(),
                entity.getTrophies()
        );
    }
}
