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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    @Column(name = "name_color", length = 45, nullable = false)
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
    @Column(name = "brawlstars_club_tag", length = 45, updatable = false, nullable = false)
    private String brawlStarsClubTag;

    @Column(name = "not_updated_count", nullable = false)
    private int notUpdatedCount;

    @Column(name = "update_weight", nullable = false)
    private LocalDateTime updateWeight;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<PlayerBrawlerCollectionEntity> brawlers = new ArrayList<>();

    @Version
    private Integer version;

    @Transient
    private Map<Long, PlayerBrawlerCollectionEntity> brawlStarsIdToBrawler;

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
            LocalDateTime now
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
        this.updateWeight = now;
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
            createBrawlerMap();
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

    private void createBrawlerMap() {
        brawlStarsIdToBrawler = brawlers.stream()
                .collect(Collectors.toMap(PlayerBrawlerCollectionEntity::getBrawlerBrawlStarsId, Function.identity()));
    }

    public boolean isNextUpdateCooldownOver(LocalDateTime now) {
        return status.isNextUpdateCooldownOver(now, getUpdatedAt());
    }

    public void battleUpdated(LocalDateTime now, List<LocalDateTime> updatedBattleTimes) {
        Optional<LocalDateTime> recentBattleOpt = updatedBattleTimes.stream()
                .min(Comparator.naturalOrder());
        if (recentBattleOpt.isEmpty()) {
            notUpdatedCount += 1;
            this.updateWeight = now.plus((long) (
                    noUpdatedCountWeight() * trophyWeight() * expLevelWeight()
            ), ChronoUnit.MILLIS);
            this.status = PlayerStatus.PLAYER_UPDATED;
            return;
        }
        notUpdatedCount = 0;
        LocalDateTime recentBattleTime = recentBattleOpt.get();
        this.updateWeight = now.plus((long) (
                recentBattleTimeWeight(Duration.between(recentBattleTime, now))
                        * battleCountWeight(updatedBattleTimes.size())
                        * trophyWeight()
                        * expLevelWeight()
        ), ChronoUnit.MILLIS);
        this.status = PlayerStatus.BATTLE_UPDATED;
    }

    private long noUpdatedCountWeight() {
        return (long) (1000000 * Math.log(notUpdatedCount + 1D) + Duration.ofHours(1).toMillis());
    }

    // f(0) = 10분, f(10분) = 20분, ... 로그함수로 증가
    private long recentBattleTimeWeight(Duration timeGap) {
        return (long) (50000 * Math.log(timeGap.toMillis() + 1D) + 600000);
    }

    // 1 ~ 2: f(25) = 2, x->1 = y->1
    private double battleCountWeight(int x) {
        return 2 / (1 + Math.exp(-(x - 25) / 2D)) + 1;
    }

    // 1.5 ~ 1L: f(0) = 1.5, x -> inf = y -> 1
    private double trophyWeight() {
        return 1 / (1 + Math.exp(trophies / 10000D)) + 1;
    }

    // 1.5 ~ 1L: f(0) = 1.5, x -> inf = y -> 1
    private double expLevelWeight() {
        return 1 / (1 + Math.exp(expLevel / 100D)) + 1;
    }

    public void deleted() {
        this.status = PlayerStatus.DELETED;
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

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

}
