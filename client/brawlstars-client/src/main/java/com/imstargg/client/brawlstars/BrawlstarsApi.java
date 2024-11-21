package com.imstargg.client.brawlstars;

import com.imstargg.client.brawlstars.request.PagingRequest;
import com.imstargg.client.brawlstars.response.BattleListResponse;
import com.imstargg.client.brawlstars.response.ClubMemberListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    /**
     * Get information about a single player by player tag.
     * Player tags can be found either in game or by from clan member list.
     *
     * @param playerTag Tag of the player.
     */
    @GetMapping(value = "/v1/players/{playerTag}", consumes = MediaType.APPLICATION_JSON_VALUE)
    PlayerResponse getPlayerInformation(@PathVariable String playerTag);

    /**
     * List club members.
     *
     * @param clubTag Tag of the club.
     */
    @GetMapping(value = "/v1/clubs/{clubTag}/members", consumes = MediaType.APPLICATION_JSON_VALUE)
    ClubMemberListResponse getListClubMembers(@PathVariable String clubTag, @ModelAttribute PagingRequest paging);
}
