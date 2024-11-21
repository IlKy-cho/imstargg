package com.imstargg.client.brawlstars.response;

import java.util.List;

public record ClubRankingListResponse(
        List<ClubRankingResponse> items,
        PagingResponse paging
) {
}
