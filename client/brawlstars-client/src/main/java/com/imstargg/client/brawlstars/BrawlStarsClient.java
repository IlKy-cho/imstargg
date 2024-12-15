package com.imstargg.client.brawlstars;

import com.imstargg.client.brawlstars.request.PagingParam;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BrawlerResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.client.brawlstars.response.ScheduledEventResponse;
import feign.FeignException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@CacheConfig(cacheNames = "brawlstars-client")
public class BrawlStarsClient {

    private final BrawlStarsApi brawlstarsApi;

    BrawlStarsClient(BrawlStarsApi brawlstarsApi) {
        this.brawlstarsApi = brawlstarsApi;
    }

    @Cacheable(key = "'brawlstars-client:player-battles:' + #playerTag")
    public ListResponse<BattleResponse> getPlayerRecentBattles(String playerTag) {
        try {
            return brawlstarsApi.getLogOfRecentBattlesForAPlayer(playerTag);
        } catch (FeignException.NotFound ex) {
            throw new BrawlStarsClientNotFoundException("playerTag=" + playerTag, ex);
        }
    }

    @Cacheable(key = "'brawlstars-client:player:' + #playerTag")
    public PlayerResponse getPlayerInformation(String playerTag) {
        try {
            return brawlstarsApi.getPlayerInformation(playerTag);
        } catch (FeignException.NotFound ex) {
            throw new BrawlStarsClientNotFoundException("playerTag=" + playerTag, ex);
        }
    }

    @Cacheable(key = "'brawlstars-client:brawlers'")
    public ListResponse<BrawlerResponse> getBrawlers() {
        return brawlstarsApi.getListOfAvailableBrawlers(PagingParam.DEFAULT);
    }

    public List<ScheduledEventResponse> getEventRotation() {
        return brawlstarsApi.getEventRotation();
    }
}
