package com.imstargg.batch.job.club;

import com.imstargg.batch.domain.NewClub;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.client.brawlstars.response.ClubResponse;
import com.imstargg.storage.db.core.club.ClubCollectionEntity;
import com.imstargg.storage.db.core.club.ClubMemberCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

public class NewClubJobItemProcessor implements ItemProcessor<String, NewClub> {

    private static final Logger log = LoggerFactory.getLogger(NewClubJobItemProcessor.class);

    private final BrawlStarsClient brawlStarsClient;

    public NewClubJobItemProcessor(BrawlStarsClient brawlStarsClient) {
        this.brawlStarsClient = brawlStarsClient;
    }

    @Override
    public NewClub process(String item) throws Exception {
        try {
            ClubResponse clubResponse = brawlStarsClient.getClub(item);
            ClubCollectionEntity clubEntity = new ClubCollectionEntity(
                    clubResponse.tag(),
                    clubResponse.name(),
                    clubResponse.description(),
                    clubResponse.type(),
                    clubResponse.badgeId(),
                    clubResponse.requiredTrophies(),
                    clubResponse.trophies()
            );
            List<ClubMemberCollectionEntity> clubMemberEntities = clubResponse.members().stream()
                    .map(clubMemberResponse -> new ClubMemberCollectionEntity(
                            clubEntity,
                            clubMemberResponse.tag(),
                            clubMemberResponse.name(),
                            clubMemberResponse.nameColor(),
                            clubMemberResponse.role(),
                            clubMemberResponse.trophies(),
                            clubMemberResponse.icon().id()
                    )).toList();
            return new NewClub(clubEntity, clubMemberEntities);
        } catch (BrawlStarsClientException.NotFound ex) {
            log.info("새 클럽 태그 {} 가 존재하지 않는 것으로 확인되어 스킵합니다.", item);
            return null;
        }
    }
}
