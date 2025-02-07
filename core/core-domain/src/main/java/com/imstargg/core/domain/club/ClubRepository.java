package com.imstargg.core.domain.club;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.ClubMemberRole;
import com.imstargg.core.enums.ClubType;
import com.imstargg.storage.db.core.club.ClubEntity;
import com.imstargg.storage.db.core.club.ClubJpaRepository;
import com.imstargg.storage.db.core.club.ClubMemberEntity;
import com.imstargg.storage.db.core.club.ClubMemberJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClubRepository {

    private final ClubJpaRepository clubJpaRepository;
    private final ClubMemberJpaRepository clubMemberJpaRepository;

    public ClubRepository(
            ClubJpaRepository clubJpaRepository,
            ClubMemberJpaRepository clubMemberJpaRepository
    ) {
        this.clubJpaRepository = clubJpaRepository;
        this.clubMemberJpaRepository = clubMemberJpaRepository;
    }

    public Optional<Club> findByTag(BrawlStarsTag tag) {
        return clubJpaRepository.findByBrawlStarsTagAndDeletedFalse(tag.value())
                .map(this::mapEntityToClub);
    }

    private Club mapEntityToClub(ClubEntity entity) {
        return new Club(
                new BrawlStarsTag(entity.getBrawlStarsTag()),
                entity.getName(),
                entity.getDescription() == null ? "" : entity.getDescription(),
                ClubType.find(entity.getType()),
                new BrawlStarsId(entity.getBadgeBrawlStarsId()),
                entity.getRequiredTrophies(),
                entity.getTrophies()
        );
    }

    public List<ClubMember> findMembers(Club club) {
        return clubMemberJpaRepository.findAllByClubBrawlStarsTagAndDeletedFalse(club.tag().value())
                .stream()
                .map(this::mapEntityToClubMember)
                .toList();
    }

    private ClubMember mapEntityToClubMember(ClubMemberEntity entity) {
        return new ClubMember(
                new BrawlStarsTag(entity.getBrawlStarsTag()),
                entity.getName(),
                entity.getNameColor(),
                ClubMemberRole.find(entity.getRole()),
                entity.getTrophies(),
                new BrawlStarsId(entity.getIconBrawlStarsId())
        );
    }
}
