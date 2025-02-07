package com.imstargg.core.domain.club;

import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClubReader {

    private final ClubRepository clubRepository;

    public ClubReader(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public Club get(BrawlStarsTag tag) {
        return clubRepository.findByTag(tag)
                .orElseThrow(() -> new CoreException(CoreErrorType.CLUB_NOT_FOUND, "clubTag=" + tag));
    }

    public List<ClubMember> getMembers(Club club) {
        return clubRepository.findMembers(club);
    }
}
