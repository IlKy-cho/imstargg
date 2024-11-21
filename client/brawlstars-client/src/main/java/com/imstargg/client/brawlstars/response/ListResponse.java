package com.imstargg.client.brawlstars.response;

import java.util.List;

public record ListResponse<T>(
        List<T> items,
        PagingResponse paging
) {
}
