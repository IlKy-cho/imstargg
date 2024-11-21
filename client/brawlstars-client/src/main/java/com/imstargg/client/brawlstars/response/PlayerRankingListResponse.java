package com.imstargg.client.brawlstars.response;

import java.util.List;

public record PlayerRankingListResponse(
        List<PlayerRankingResponse> items,
        PagingResponse paging
) {
}
