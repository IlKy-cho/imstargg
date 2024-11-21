package com.imstargg.client.brawlstars;

import com.imstargg.client.brawlstars.request.PagingParam;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BrawlerResponse;
import com.imstargg.client.brawlstars.response.ClubMemberResponse;
import com.imstargg.client.brawlstars.response.ClubRankingResponse;
import com.imstargg.client.brawlstars.response.ClubResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerRankingResponse;
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
    ListResponse<BattleResponse> getLogOfRecentBattlesForAPlayer(@PathVariable String playerTag);

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
    ListResponse<ClubMemberResponse> getListClubMembers(@PathVariable String clubTag, @ModelAttribute PagingParam paging);

    /**
     * Get information about a single clan by club tag.
     * Club tags can be found in game.
     * Note that clan tags start with hash character '#' and that needs to be URL-encoded properly to work in URL,
     * so for example clan tag '#2ABC' would become '%232ABC' in the URL.
     *
     * @param clubTag Tag of the club.
     */
    @GetMapping(value = "/v1/clubs/{clubTag}", consumes = MediaType.APPLICATION_JSON_VALUE)
    ClubResponse getClubInformation(@PathVariable String clubTag);

    /**
     * Get club rankings for a country or global rankings.
     *
     * @param countryCode Two letter country code, or 'global' for global rankings.
     */
    @GetMapping(value = "/v1/rankings/{countryCode}/clubs", consumes = MediaType.APPLICATION_JSON_VALUE)
    ListResponse<ClubRankingResponse> getClubRankingsForACountryOrGlobalRankings(
            @PathVariable String countryCode, @ModelAttribute PagingParam paging);


    /**
     * Get brawler rankings for a country or global rankings.
     * Brawler identifiers can be found by using the /v1/brawlers API endpoint.
     *
     * @param countryCode Two letter country code, or 'global' for global rankings.
     * @param brawlerId Identifier of the brawler.
     */
    @GetMapping(value = "/v1/rankings/{countryCode}/brawlers/{brawlerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    ListResponse<PlayerRankingResponse> getBrawlerRankingsForACountryOrGlobalRankings(
            @PathVariable String countryCode, @PathVariable String brawlerId, @ModelAttribute PagingParam paging);

    /**
     * Get player rankings for a country or global rankings.
     *
     * @param countryCode Two letter country code, or 'global' for global rankings.
     */
    @GetMapping(value = "/v1/rankings/{countryCode}/players", consumes = MediaType.APPLICATION_JSON_VALUE)
    ListResponse<PlayerRankingResponse> getPlayerRankingsForACountryOrGlobalRankings(
            @PathVariable String countryCode, @ModelAttribute PagingParam paging);

    @GetMapping(value = "/v1/brawlers", consumes = MediaType.APPLICATION_JSON_VALUE)
    ListResponse<BrawlerResponse> getListOfAvailableBrawlers(@ModelAttribute PagingParam paging);

    @GetMapping(value = "/v1/brawlers/{brawlerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    BrawlerResponse getInformationAboutABrawler(@PathVariable String brawlerId);
}
