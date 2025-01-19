package com.imstargg.storage.db.core;

import com.imstargg.core.enums.BattleType;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "battle")
public class BattleCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_id", nullable = false)
    private Long id;

    @Column(name = "battle_key", updatable = false, nullable = false)
    private String battleKey;

    @Column(name = "battle_time", updatable = false, nullable = false)
    private OffsetDateTime battleTime;

    @Embedded
    private BattleCollectionEntityEvent event;

    @Column(name = "mode", length = 105, updatable = false, nullable = false)
    private String mode;

    @Nullable
    @Column(name = "type", length = 105, updatable = false)
    private String type;

    @Nullable
    @Column(name = "result", length = 25, updatable = false)
    private String result;

    @Nullable
    @Column(name = "duration", updatable = false)
    private Integer duration;

    @Nullable
    @Column(name = "star_player_brawlstars_tag", length = 45, updatable = false)
    private String starPlayerBrawlStarsTag;

    @Embedded
    private BattleCollectionEntityPlayer player;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "teams", columnDefinition = "json", updatable = false, nullable = false)
    private List<List<BattleCollectionEntityTeamPlayer>> teams;

    protected BattleCollectionEntity() {
    }

    public BattleCollectionEntity(
            String battleKey,
            OffsetDateTime battleTime,
            BattleCollectionEntityEvent event,
            String mode,
            String type,
            @Nullable String result,
            @Nullable Integer duration,
            @Nullable String starPlayerBrawlStarsTag,
            BattleCollectionEntityPlayer player,
            List<List<BattleCollectionEntityTeamPlayer>> teams
    ) {
        this.battleKey = battleKey;
        this.battleTime = battleTime;
        this.event = event;
        this.mode = mode;
        this.type = type;
        this.result = result;
        this.duration = duration;
        this.starPlayerBrawlStarsTag = starPlayerBrawlStarsTag;
        this.player = player;
        this.teams = teams;
    }

    public boolean existsEventId() {
        return event.getBrawlStarsId() != null && event.getBrawlStarsId() > 0;
    }

    public boolean amIStarPlayer() {
        return Objects.equals(
                this.getStarPlayerBrawlStarsTag(),
                this.getPlayer().getPlayer().getBrawlStarsTag()
        );
    }

    public List<BattleCollectionEntityTeamPlayer> findMe() {
        return teams.stream()
                .flatMap(List::stream)
                .filter(teamPlayer -> Objects.equals(
                        teamPlayer.getBrawlStarsTag(), this.getPlayer().getPlayer().getBrawlStarsTag())
                ).toList();
    }

    public Optional<BattleCollectionEntityTeamPlayer> findStarPlayer() {
        return teams.stream()
                .flatMap(List::stream)
                .filter(teamPlayer -> Objects.equals(
                        teamPlayer.getBrawlStarsTag(), this.getStarPlayerBrawlStarsTag())
                ).findFirst();
    }

    public List<BattleCollectionEntityTeamPlayer> findMyTeam() {
        return teams.stream()
                .filter(team -> team.stream()
                        .anyMatch(teamPlayer -> Objects.equals(
                                teamPlayer.getBrawlStarsTag(),
                                this.getPlayer().getPlayer().getBrawlStarsTag())
                        )
                ).findFirst()
                .orElseThrow(() -> new IllegalStateException("내 팀이 없습니다. battleId: " + id));
    }

    public List<List<BattleCollectionEntityTeamPlayer>> findEnemyTeams() {
        return teams.stream()
                .filter(team -> team.stream()
                        .noneMatch(teamPlayer -> Objects.equals(
                                teamPlayer.getBrawlStarsTag(),
                                this.getPlayer().getPlayer().getBrawlStarsTag())
                        )
                ).toList();
    }

    public List<BattleCollectionEntityTeamPlayer> findEnemyTeam() {
        List<List<BattleCollectionEntityTeamPlayer>> enemyTeams = findEnemyTeams();
        if (enemyTeams.size() != 1) {
            throw new IllegalStateException("적 팀이 1개가 아닙니다. battleId: " + id);
        }

        return enemyTeams.getFirst();
    }

    public List<BattlePlayerCombination> playerCombinations() {
        return findMe().stream()
                .flatMap(me -> findEnemyTeams().stream()
                        .flatMap(Collection::stream)
                        .map(enemy -> new BattlePlayerCombination(me, enemy))
                ).toList();
    }

    public List<BattleMyTeamCombination> myTeamCombinations() {
        return new BattlePlayerCombinationBuilder(findMyTeam()).build().stream()
                .map(BattleMyTeamCombination::new)
                .toList();
    }

    public boolean containsDuplicateBrawler() {
        List<Long> brawlerIds = teams.stream()
                .flatMap(List::stream)
                .map(BattleCollectionEntityTeamPlayer::getBrawler)
                .map(BattleCollectionEntityTeamPlayerBrawler::getBrawlStarsId)
                .toList();

        return brawlerIds.size() != brawlerIds.stream().distinct().count();
    }

    public List<BattleCollectionEntityTeamPlayer> getAllPlayers() {
        return teams.stream()
                .flatMap(List::stream)
                .toList();
    }

    public boolean canStatisticsCollected() {
        return result != null
                && existsEventId()
                && BattleType.find(type).isRegular()
                && teams.size() == 2;
    }

    public Long getId() {
        return id;
    }

    public String getBattleKey() {
        return battleKey;
    }

    public OffsetDateTime getBattleTime() {
        return battleTime;
    }

    public BattleCollectionEntityEvent getEvent() {
        return event;
    }

    public String getMode() {
        return mode;
    }

    @Nullable
    public String getType() {
        return type;
    }

    @Nullable
    public String getResult() {
        return result;
    }

    @Nullable
    public Integer getDuration() {
        return duration;
    }

    @Nullable
    public String getStarPlayerBrawlStarsTag() {
        return starPlayerBrawlStarsTag;
    }

    public BattleCollectionEntityPlayer getPlayer() {
        return player;
    }

    public List<List<BattleCollectionEntityTeamPlayer>> getTeams() {
        return teams;
    }

}
