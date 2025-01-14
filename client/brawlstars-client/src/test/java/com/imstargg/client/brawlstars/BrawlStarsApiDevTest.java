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
        "app.client.brawlstars.keys=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6IjdjODNiMGE1LTg0ZjItNDYzNS1hMzAwLTk4NjlmYmQ2MTFlNyIsImlhdCI6MTczMzIwNjEyNywic3ViIjoiZGV2ZWxvcGVyLzk4MjY5Yzk4LTg0ZjctNzE0ZC00NzIyLTczMmFkNzI3MTdkOSIsInNjb3BlcyI6WyJicmF3bHN0YXJzIl0sImxpbWl0cyI6W3sidGllciI6ImRldmVsb3Blci9zaWx2ZXIiLCJ0eXBlIjoidGhyb3R0bGluZyJ9LHsiY2lkcnMiOlsiMjIxLjE1My4yMTYuMjMyIiwiMTc1LjE5NS4xNjUuNDUiXSwidHlwZSI6ImNsaWVudCJ9XX0.UwVMSOaXEPqvehWlcSs5lzzc2fG3p5-oxu3ssXsEBgfJ2DHyLpGKMlBMwJlPW7xijCmJ5IpksfkCf0T9wHSleA"
})
class BrawlStarsApiDevTest {

    @Autowired
    private BrawlStarsApi brawlstarsApi;

    private final String playerTag = "#88J8Q9RPV";
    private final String clubTag = "";
    private final long brawlerId = 0;

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
