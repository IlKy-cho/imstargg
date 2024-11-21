package com.imstargg.client.brawlstars.response;

import java.util.List;

public record ClubMemberListResponse(
        List<ClubMemberResponse> items,
        PagingResponse paging
) {
}
