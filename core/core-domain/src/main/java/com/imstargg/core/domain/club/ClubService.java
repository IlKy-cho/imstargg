package com.imstargg.core.domain.club;

import com.imstargg.core.domain.BrawlStarsTag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {

    private final ClubReader clubReader;

    public ClubService(ClubReader clubReader) {
        this.clubReader = clubReader;
    }

    public Club get(BrawlStarsTag tag) {
        return clubReader.get(tag);
    }

    public List<ClubMember> getMembers(BrawlStarsTag tag) {
        return clubReader.getMembers(clubReader.get(tag));
    }
}
