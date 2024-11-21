package com.imstargg.client.brawlstars.request;

import jakarta.annotation.Nullable;

/**
 * @param before
 * Return only items that occur before this marker.
 * Before marker can be found from the response, inside the 'paging' property.
 * Note that only after or before can be specified for a request, not both.
 *
 * @param after
 * Return only items that occur after this marker.
 * Before marker can be found from the response, inside the 'paging' property.
 * Note that only after or before can be specified for a request, not both.
 *
 * @param limit
 * Limit the number of items returned in the response.
 */
public record PagingRequest(
        @Nullable String before,
        @Nullable String after,
        @Nullable Integer limit
) {
}
