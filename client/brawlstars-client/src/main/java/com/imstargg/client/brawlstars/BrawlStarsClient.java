package com.imstargg.client.brawlstars;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imstargg.client.brawlstars.request.PagingParam;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BrawlerResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.client.brawlstars.response.ScheduledEventResponse;
import feign.FeignException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.io.UncheckedIOException;
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
        } catch (FeignException.ServiceUnavailable ex) {
            if (isInMaintenance(ex)) {
                throw new BrawlStarsClientException.InMaintenance("현재 서비스 점검 중입니다.", ex);
            }
            throw ex;
        }
    }

    @Cacheable(key = "'brawlstars-client:player:' + #playerTag")
    public PlayerResponse getPlayerInformation(String playerTag) {
        try {
            return brawlstarsApi.getPlayerInformation(playerTag);
        } catch (FeignException.NotFound ex) {
            throw new BrawlStarsClientException.NotFound("playerTag=" + playerTag, ex);
        } catch (FeignException.ServiceUnavailable ex) {
            if (isInMaintenance(ex)) {
                throw new BrawlStarsClientException.InMaintenance("현재 서비스 점검 중입니다.", ex);
            }
            throw ex;
        }
    }

    private boolean isInMaintenance(FeignException.ServiceUnavailable ex) {
        return ex.responseBody()
                .map(body -> {
                    try {
                        return objectMapper.readValue(body.array(), BrawlStarsClientErrorResponse.class);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .filter(BrawlStarsClientErrorResponse::isInMaintenance)
                .isPresent();
    }

    @Cacheable(key = "'brawlstars-client:brawlers'")
    public ListResponse<BrawlerResponse> getBrawlers() {
        return brawlstarsApi.getListOfAvailableBrawlers(PagingParam.DEFAULT);
    }

    public List<ScheduledEventResponse> getEventRotation() {
        return brawlstarsApi.getEventRotation();
    }
}
