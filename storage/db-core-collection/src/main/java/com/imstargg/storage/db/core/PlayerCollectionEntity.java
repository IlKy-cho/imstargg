package com.imstargg.storage.db.core;

import com.imstargg.core.enums.PlayerStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@Table(name = "player")
public class PlayerCollectionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    @Column(name = "brawlstars_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(45)", nullable = false)
    private PlayerStatus status;

    @Column(name = "name", length = 105, nullable = false)
    private String name;

    @Nullable
    @Column(name = "name_color", length = 45)
    private String nameColor;

    @Column(name = "icon_brawlstars_id", nullable = false)
    private long iconBrawlStarsId;

    @Column(name = "trophies", nullable = false)
    private int trophies;

    @Column(name = "highest_trophies", nullable = false)
    private int highestTrophies;

    @Column(name = "exp_level", nullable = false)
    private int expLevel;

    @Column(name = "exp_points", nullable = false)
    private int expPoints;

    @Column(name = "qualified_from_championship_challenge", nullable = false)
    private boolean qualifiedFromChampionshipChallenge;

    @Column(name = "victories_3vs3", nullable = false)
    private int victories3vs3;

    @Column(name = "solo_victories", nullable = false)
    private int soloVictories;

    @Column(name = "duo_victories", nullable = false)
    private int duoVictories;

    @Column(name = "best_robo_rumble_time", nullable = false)
    private int bestRoboRumbleTime;

    @Column(name = "best_time_as_big_brawler", nullable = false)
    private int bestTimeAsBigBrawler;

    @Nullable
    @Column(name = "brawlstars_club_tag", length = 45, updatable = false)
    private String brawlStarsClubTag;

    @Column(name = "not_updated_count", nullable = false)
    private int notUpdatedCount;

    @Column(name = "update_weight", nullable = false)
    private LocalDateTime updateWeight;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<PlayerBrawlerCollectionEntity> brawlers = new ArrayList<>();

    @Transient
    private Map<Long, PlayerBrawlerCollectionEntity> brawlStarsIdToBrawler;

    @Nullable
    @Column(name = "latest_battle_time")
    private LocalDateTime latestBattleTime;

    @Version
    @Column(name = "version", nullable = false)
    private int version;

    protected PlayerCollectionEntity() {
    }

    public PlayerCollectionEntity(
            String brawlStarsTag,
            String name,
            String nameColor,
            long iconBrawlStarsId,
            int trophies,
            int highestTrophies,
            int expLevel,
            int expPoints,
            boolean qualifiedFromChampionshipChallenge,
            int victories3vs3,
            int soloVictories,
            int duoVictories,
            int bestRoboRumbleTime,
            int bestTimeAsBigBrawler,
            @Nullable String brawlStarsClubTag,
            Clock clock
    ) {
        this.status = PlayerStatus.NEW;
        this.brawlStarsTag = brawlStarsTag;
        this.name = name;
        this.nameColor = nameColor;
        this.iconBrawlStarsId = iconBrawlStarsId;
        this.trophies = trophies;
        this.highestTrophies = highestTrophies;
        this.expLevel = expLevel;
        this.expPoints = expPoints;
        this.qualifiedFromChampionshipChallenge = qualifiedFromChampionshipChallenge;
        this.victories3vs3 = victories3vs3;
        this.soloVictories = soloVictories;
        this.duoVictories = duoVictories;
        this.bestRoboRumbleTime = bestRoboRumbleTime;
        this.bestTimeAsBigBrawler = bestTimeAsBigBrawler;
        this.brawlStarsClubTag = brawlStarsClubTag;
        this.notUpdatedCount = 0;
        this.updateWeight = LocalDateTime.now(clock);
    }

    public void updateBrawler(
            long brawlerBrawlStarsId,
            int power,
            int rank,
            int trophies,
            int highestTrophies,
            List<Long> gearBrawlStarsIds,
            List<Long> starPowerBrawlStarsIds,
            List<Long> gadgetBrawlStarsIds
    ) {
        if (brawlStarsIdToBrawler == null) {
            initializeBrawlStarsIdToBrawler();
        }

        if (brawlStarsIdToBrawler.containsKey(brawlerBrawlStarsId)) {
            brawlStarsIdToBrawler.get(brawlerBrawlStarsId).update(
                    power,
                    rank,
                    trophies,
                    highestTrophies,
                    gearBrawlStarsIds,
                    starPowerBrawlStarsIds,
                    gadgetBrawlStarsIds
            );
        } else {
            PlayerBrawlerCollectionEntity brawler = new PlayerBrawlerCollectionEntity(
                    this,
                    brawlerBrawlStarsId,
                    power,
                    rank,
                    trophies,
                    highestTrophies,
                    gearBrawlStarsIds,
                    starPowerBrawlStarsIds,
                    gadgetBrawlStarsIds
            );
            brawlers.add(brawler);
            brawlStarsIdToBrawler.put(brawlerBrawlStarsId, brawler);
        }
    }

    public void initializeBrawlStarsIdToBrawler() {
        brawlStarsIdToBrawler = brawlers.stream()
                .collect(Collectors.toMap(PlayerBrawlerCollectionEntity::getBrawlerBrawlStarsId, Function.identity()));
    }

    public boolean isNextUpdateCooldownOver(LocalDateTime now) {
        return status.isNextUpdateCooldownOver(now, getUpdatedAt());
    }

    public void battleUpdated(Clock clock, List<LocalDateTime> updatedBattleTimes) {
        LocalDateTime now = LocalDateTime.now(clock);
        Optional<LocalDateTime> latestBattleTimeOpt = findLatestBattleTime(updatedBattleTimes);

        if (latestBattleTimeOpt.isEmpty()) {
            handleNoBattleUpdate(now);
            return;
        }

        handleBattleUpdate(latestBattleTimeOpt.get(), now);
    }

    private Optional<LocalDateTime> findLatestBattleTime(List<LocalDateTime> battleTimes) {
        return battleTimes.stream()
                .max(Comparator.naturalOrder());
    }

    private void handleNoBattleUpdate(LocalDateTime now) {
        if (checkDormant(now)) {
            this.status = PlayerStatus.DORMANT;
            return;
        }
        this.updateWeight = nextUpdateTimeWhenNotBattleUpdated(now);
        this.status = PlayerStatus.PLAYER_UPDATED;
    }

    private void handleBattleUpdate(LocalDateTime latestBattleTime, LocalDateTime now) {
        this.status = determineNewStatus();
        this.latestBattleTime = latestBattleTime;
        this.updateWeight = nextUpdateTimeWhenBattleUpdated(now, latestBattleTime);
    }

    private PlayerStatus determineNewStatus() {
        return this.status == PlayerStatus.NEW ? 
               PlayerStatus.PLAYER_UPDATED : 
               PlayerStatus.BATTLE_UPDATED;
    }

    private LocalDateTime nextUpdateTimeWhenBattleUpdated(
            LocalDateTime now, LocalDateTime latestBattleTime) {
        if (isRecentBattle(latestBattleTime, now)) {
            return calculateWeightedUpdateTime(now);
        }
        return now.plusHours(12);
    }

    private boolean isRecentBattle(LocalDateTime battleTime, LocalDateTime now) {
        return Duration.between(battleTime, now).toMinutes() < 30;
    }

    private LocalDateTime calculateWeightedUpdateTime(LocalDateTime now) {
        long weightMultiplier = (long) trophyWeight() * expLevelWeight();
        return now.plus(Duration.ofMinutes(60).multipliedBy(weightMultiplier));
    }

    private boolean checkDormant(LocalDateTime now) {
        return durationBetweenLastBattleUpdated(now).toDays() > 30;
    }

    private Duration durationBetweenLastBattleUpdated(LocalDateTime now) {
        return Duration.between(
                latestBattleTime != null ? latestBattleTime : getCreatedAt(),
                now
        );
    }

    private LocalDateTime nextUpdateTimeWhenNotBattleUpdated(LocalDateTime now) {
        Duration term = Duration.ofHours(12);
        Duration lastUpdatedDuration = durationBetweenLastBattleUpdated(now);
        while (term.toDays() <= 30) {
            if (term.compareTo(lastUpdatedDuration) >= 0) {
                break;
            }
            term = term.multipliedBy(2);
        }

        return now.plus(term);
    }

    private int trophyWeight() {
        if (trophies < 10000) {
            return 4;
        } else if (trophies < 20000) {
            return 3;
        } else if (trophies < 30000) {
            return 2;
        }

        return 1;
    }

    private int expLevelWeight() {
        if (expLevel < 50) {
            return 3;
        } else if (expLevel < 100) {
            return 2;
        }

        return 1;
    }

    public void deleted() {
        this.status = PlayerStatus.DELETED;
    }

    public void renewRequested() {
        this.status = PlayerStatus.RENEW_REQUESTED;
    }

    public void update(
            String name,
            String nameColor,
            long iconBrawlStarsId,
            int trophies,
            int highestTrophies,
            int expLevel,
            int expPoints,
            boolean qualifiedFromChampionshipChallenge,
            int victories3vs3,
            int soloVictories,
            int duoVictories,
            int bestRoboRumbleTime,
            int bestTimeAsBigBrawler,
            @Nullable String brawlStarsClubTag
    ) {
        this.status = PlayerStatus.PLAYER_UPDATED;
        this.name = name;
        this.nameColor = nameColor;
        this.iconBrawlStarsId = iconBrawlStarsId;
        this.trophies = trophies;
        this.highestTrophies = highestTrophies;
        this.expLevel = expLevel;
        this.expPoints = expPoints;
        this.qualifiedFromChampionshipChallenge = qualifiedFromChampionshipChallenge;
        this.victories3vs3 = victories3vs3;
        this.soloVictories = soloVictories;
        this.duoVictories = duoVictories;
        this.bestRoboRumbleTime = bestRoboRumbleTime;
        this.bestTimeAsBigBrawler = bestTimeAsBigBrawler;
        this.brawlStarsClubTag = brawlStarsClubTag;
    }

    public Long getId() {
        return id;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public String getBrawlStarsTag() {
        return brawlStarsTag;
    }

    public String getName() {
        return name;
    }

    public String getNameColor() {
        return nameColor;
    }

    public long getIconBrawlStarsId() {
        return iconBrawlStarsId;
    }

    public int getTrophies() {
        return trophies;
    }

    public int getHighestTrophies() {
        return highestTrophies;
    }

    public int getExpLevel() {
        return expLevel;
    }

    public int getExpPoints() {
        return expPoints;
    }

    public boolean isQualifiedFromChampionshipChallenge() {
        return qualifiedFromChampionshipChallenge;
    }

    public int getVictories3vs3() {
        return victories3vs3;
    }

    public int getSoloVictories() {
        return soloVictories;
    }

    public int getDuoVictories() {
        return duoVictories;
    }

    public int getBestRoboRumbleTime() {
        return bestRoboRumbleTime;
    }

    public int getBestTimeAsBigBrawler() {
        return bestTimeAsBigBrawler;
    }

    @Nullable
    public String getBrawlStarsClubTag() {
        return brawlStarsClubTag;
    }

    public int getNotUpdatedCount() {
        return notUpdatedCount;
    }

    public LocalDateTime getUpdateWeight() {
        return updateWeight;
    }

    public List<PlayerBrawlerCollectionEntity> getBrawlers() {
        return brawlers;
    }

    @Nullable
    public LocalDateTime getLatestBattleTime() {
        return latestBattleTime;
    }
}
