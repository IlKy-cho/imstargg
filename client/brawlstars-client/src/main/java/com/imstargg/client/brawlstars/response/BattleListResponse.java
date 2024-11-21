package com.imstargg.client.brawlstars.response;

import java.util.List;

public record BattleListResponse(
        List<BattleResponse> items,
        PagingResponse paging
) {
}
