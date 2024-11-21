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
import com.imstargg.client.brawlstars.response.ScheduledEventResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@Tag("develop")
@TestPropertySource(properties = {
        "app.client.brawlstars.key="
})
class BrawlstarsApiDevTest {

    @Autowired
    private BrawlstarsApi brawlstarsApi;

    private String playerTag = "";
    private String clubTag = "";
    private long brawlerId = 0;

    @Test
    void getLogOfRecentBattlesForAPlayer() {
        ListResponse<BattleResponse> response = brawlstarsApi
                .getLogOfRecentBattlesForAPlayer(playerTag);
        System.out.println(response);
    }

    @Test
    void getPlayerInformation() {
        PlayerResponse response = brawlstarsApi
                .getPlayerInformation(playerTag);
        System.out.println(response);
    }

    @Test
    void getListClubMembers() {
        ListResponse<ClubMemberResponse> response = brawlstarsApi
                .getListClubMembers(clubTag, PagingParam.DEFAULT);
        System.out.println(response);
    }

    @Test
    void getClubInformation()  {
        ClubResponse response = brawlstarsApi.getClubInformation(clubTag);
        System.out.println(response);
    }

    @Test
    void getClubRankingsForACountryOrGlobalRankings() {
        ListResponse<ClubRankingResponse> response = brawlstarsApi
                .getClubRankingsForACountryOrGlobalRankings("global", PagingParam.DEFAULT);
        System.out.println(response);
    }

    @Test
    void getBrawlerRankingsForACountryOrGlobalRankings() {
        ListResponse<PlayerRankingResponse> response = brawlstarsApi
                .getBrawlerRankingsForACountryOrGlobalRankings("global", brawlerId, PagingParam.DEFAULT);
        System.out.println(response);
    }

    @Test
    void getPlayerRankingsForACountryOrGlobalRankings() {
        ListResponse<PlayerRankingResponse> response = brawlstarsApi
                .getPlayerRankingsForACountryOrGlobalRankings("global", PagingParam.DEFAULT);
        System.out.println(response);
    }

    @Test
    void getListOfAvailableBrawlers() {
        ListResponse<BrawlerResponse> response = brawlstarsApi
                .getListOfAvailableBrawlers(PagingParam.DEFAULT);
        System.out.println(response);
    }

    @Test
    void getInformationAboutABrawler() {
        BrawlerResponse response = brawlstarsApi
                .getInformationAboutABrawler(brawlerId);
        System.out.println(response);
    }

    @Test
    void getEventRotation() {
        List<ScheduledEventResponse> response = brawlstarsApi.getEventRotation();
        System.out.println(response);
    }
}
