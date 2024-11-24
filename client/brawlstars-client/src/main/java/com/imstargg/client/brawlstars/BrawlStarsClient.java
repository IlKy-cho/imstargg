package com.imstargg.client.brawlstars;

import com.imstargg.client.brawlstars.request.PagingParam;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BrawlerResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.client.brawlstars.response.ScheduledEventResponse;
import com.imstargg.support.ratelimit.RateLimiter;
import feign.FeignException;

import java.util.List;

public class BrawlStarsClient {

    private final BrawlStarsApi brawlstarsApi;
    private final RateLimiter rateLimiter;

    BrawlStarsClient(BrawlStarsApi brawlstarsApi, RateLimiter rateLimiter) {
        this.brawlstarsApi = brawlstarsApi;
        this.rateLimiter = rateLimiter;
    }

    public ListResponse<BattleResponse> getPlayerRecentBattles(String playerTag) {
        rateLimiter.acquire();
        try {
            return brawlstarsApi.getLogOfRecentBattlesForAPlayer(playerTag);
        } catch (FeignException.NotFound ex) {
            throw new BrawlStarsClientNotFoundException("playerTag=" + playerTag, ex);
        }
    }

    public PlayerResponse getPlayerInformation(String playerTag) {
        rateLimiter.acquire();
        try {
            return brawlstarsApi.getPlayerInformation(playerTag);
        } catch (FeignException.NotFound ex) {
            throw new BrawlStarsClientNotFoundException("playerTag=" + playerTag, ex);
        }
    }

    public ListResponse<BrawlerResponse> getBrawlers() {
        rateLimiter.acquire();
        return brawlstarsApi.getListOfAvailableBrawlers(PagingParam.DEFAULT);
    }

    public List<ScheduledEventResponse> getEventRotation() {
        return brawlstarsApi.getEventRotation();
    }
}
