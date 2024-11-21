package com.imstargg.client.brawlstars;

import com.imstargg.client.brawlstars.response.BattleListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        value = "brawlstars",
        url = "${app.client.brawlstars.api.url}",
        configuration = BrawlstarsClientConfig.class
)
public interface BrawlstarsApi {

    /**
     * Get list of recent battle results for a player.
     * NOTE: It may take up to 30 minutes for a new battle to appear in the battlelog.
     *
     * @param playerTag Tag of the player.
     */
    @GetMapping(value = "/v1/players/{playerTag}/battlelog", consumes = MediaType.APPLICATION_JSON_VALUE)
    BattleListResponse getLogOfRecentBattlesForAPlayer(@PathVariable String playerTag);

}
