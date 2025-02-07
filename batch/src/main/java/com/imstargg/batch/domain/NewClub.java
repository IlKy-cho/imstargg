package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.club.ClubCollectionEntity;
import com.imstargg.storage.db.core.club.ClubMemberCollectionEntity;

import java.util.List;

public record NewClub(
        ClubCollectionEntity club,
        List<ClubMemberCollectionEntity> clubMembers
) {
}
