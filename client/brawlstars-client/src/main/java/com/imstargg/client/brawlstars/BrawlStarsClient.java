package com.imstargg.client.brawlstars;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imstargg.client.brawlstars.request.PagingParam;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BrawlerResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerRankingResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.client.brawlstars.response.ScheduledEventResponse;
import feign.FeignException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@CacheConfig(cacheNames = BrawlStarsClientCacheNames.BRAWLSTARS_CLIENT)
public class BrawlStarsClient {

    private final BrawlStarsApi brawlstarsApi;

    private final ObjectMapper objectMapper = new ObjectMapper();

    BrawlStarsClient(BrawlStarsApi brawlstarsApi) {
        this.brawlstarsApi = brawlstarsApi;
    }

    @Cacheable(key = "'brawlstars-client:player-battles:' + #playerTag")
    public ListResponse<BattleResponse> getPlayerRecentBattles(String playerTag) {
        try {
            return brawlstarsApi.getLogOfRecentBattlesForAPlayer(playerTag);
        } catch (FeignException.NotFound ex) {
            throw new BrawlStarsClientException.NotFound("playerTag=" + playerTag, ex);
        }
    }

    @Cacheable(key = "'brawlstars-client:player:' + #playerTag")
    public PlayerResponse getPlayerInformation(String playerTag) {
        try {
            return brawlstarsApi.getPlayerInformation(playerTag);
        } catch (FeignException.NotFound ex) {
            throw new BrawlStarsClientException.NotFound("playerTag=" + playerTag, ex);
        }
    }

    @Cacheable(key = "'brawlstars-client:brawlers'")
    public ListResponse<BrawlerResponse> getBrawlers() {
        return brawlstarsApi.getListOfAvailableBrawlers(PagingParam.DEFAULT);
    }

    public List<ScheduledEventResponse> getEventRotation() {
        return brawlstarsApi.getEventRotation();
    }

    public ListResponse<PlayerRankingResponse> getPlayerRanking(String country) {
        return brawlstarsApi.getPlayerRankingsForACountryOrGlobalRankings(country, PagingParam.DEFAULT);
    }

    public ListResponse<PlayerRankingResponse> getBrawlerRanking(String country, long brawlerId) {
        return brawlstarsApi.getBrawlerRankingsForACountryOrGlobalRankings(country, brawlerId, PagingParam.DEFAULT);
    }
}
