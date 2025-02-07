package com.imstargg.storage.db.core.club;

import java.util.List;

interface ClubMemberJpaRepositoryCustom {

    List<ClubMemberEntity> findAllByClubBrawlStarsTagAndDeletedFalse(String clubBrawlStarsTag);
}
