package com.imstargg.client.brawlstars;

import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BrawlerResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.client.brawlstars.response.ScheduledEventResponse;
import feign.FeignException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BrawlstarsClient {

    private final BrawlstarsApi brawlstarsApi;

    BrawlstarsClient(BrawlstarsApi brawlstarsApi) {
        this.brawlstarsApi = brawlstarsApi;
    }

    public ListResponse<BattleResponse> getPlayerRecentBattles(String playerTag) {
        try {
            return brawlstarsApi.getLogOfRecentBattlesForAPlayer(playerTag);
        } catch (FeignException.NotFound ex) {
            throw new BrawlstarsClientNotFoundException("playerTag=" + playerTag, ex);
        }
    }

    public PlayerResponse getPlayerInformation(String playerTag) {
        try {
            return brawlstarsApi.getPlayerInformation(playerTag);
        } catch (FeignException.NotFound ex) {
            throw new BrawlstarsClientNotFoundException("playerTag=" + playerTag, ex);
        }
    }

    public ListResponse<BrawlerResponse> getBrawlers() {
        return brawlstarsApi.getListOfAvailableBrawlers(null);
    }

    public List<ScheduledEventResponse> getEventRotation() {
        return brawlstarsApi.getEventRotation();
    }
}
