package com.imstargg.storage.db.core.player;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.BaseEntity;
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
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    @Column(name = "brawlstars_club_tag", length = 45)
    private String brawlStarsClubTag;

    @Nullable
    @Column(name = "latest_battle_time")
    private OffsetDateTime latestBattleTime;

    @Nullable
    @Column(name = "solo_rank_tier")
    private Integer soloRankTier;

    @Version
    @Column(name = "version", nullable = false)
    private int version;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private final List<PlayerBrawlerCollectionEntity> brawlers = new ArrayList<>();

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
            @Nullable String brawlStarsClubTag
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
    }

    public PlayerCollectionEntity(
            String brawlStarsTag
    ) {
        this.status = PlayerStatus.NEW;
        this.brawlStarsTag = brawlStarsTag;
    }

    public boolean updateBrawler(
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
            return brawlStarsIdToBrawler.get(brawlerBrawlStarsId).update(
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
            return true;
        }
    }

    public void initializeBrawlStarsIdToBrawler() {
        brawlStarsIdToBrawler = brawlers.stream()
                .collect(Collectors.toMap(PlayerBrawlerCollectionEntity::getBrawlerBrawlStarsId, Function.identity()));
    }

    public void playerUpdated(Clock clock, List<BattleCollectionEntity> battles) {
        if (this.status == PlayerStatus.DELETED) {
            return;
        }

        updateLatestBattleTime(battles);
        updateStatus(clock);
        updateSoloRankTier(battles);
    }

    public List<BattleCollectionEntity> battleUpdated(Clock clock, List<BattleCollectionEntity> battles) {
        battles = filterBattles(battles);
        updateLatestBattleTime(battles);
        updateSoloRankTier(battles);
        updateStatus(clock);
        return battles;
    }

    private List<BattleCollectionEntity> filterBattles(List<BattleCollectionEntity> battles) {
        return Optional.ofNullable(this.latestBattleTime)
                .map(latest -> battles.stream()
                        .filter(battle -> battle.getBattleTime().isAfter(latest))
                        .toList()
                ).orElse(battles);
    }

    private void updateLatestBattleTime(List<BattleCollectionEntity> battles) {
        battles.stream()
                .map(BattleCollectionEntity::getBattleTime)
                .max(Comparator.naturalOrder())
                .ifPresent(battleTime -> this.latestBattleTime = battleTime);
    }

    private void updateStatus(Clock clock) {
        if (durationBetweenLastBattle(clock).toDays() > 30) {
            this.status = PlayerStatus.DORMANT;
        } else {
            this.status = PlayerStatus.PLAYER_UPDATED;
        }
    }

    private void updateSoloRankTier(List<BattleCollectionEntity> battles) {
        battles
                .stream()
                .filter(battle -> Objects.equals(battle.getType(), BattleType.SOLO_RANKED.getCode()))
                .max(Comparator.comparing(BattleCollectionEntity::getBattleTime))
                .map(battle -> battle.findMe().getFirst())
                .ifPresent(latestSoloRankBattlePlayer ->
                        this.soloRankTier = latestSoloRankBattlePlayer.getBrawler().getTrophies()
                );
    }

    private Duration durationBetweenLastBattle(Clock clock) {
        OffsetDateTime now = OffsetDateTime.now(clock);
        OffsetDateTime latestBattleTimeOrNow = Optional.ofNullable(latestBattleTime)
                .or(() -> Optional.ofNullable(getCreatedAt()))
                .orElse(now);
        return Duration.between(latestBattleTimeOrNow, now);
    }

    public boolean update(
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
        boolean updated = !name.equals(this.name)
                || !Objects.equals(nameColor, this.nameColor)
                || iconBrawlStarsId != this.iconBrawlStarsId
                || trophies != this.trophies
                || highestTrophies != this.highestTrophies
                || expLevel != this.expLevel
                || expPoints != this.expPoints
                || qualifiedFromChampionshipChallenge != this.qualifiedFromChampionshipChallenge
                || victories3vs3 != this.victories3vs3
                || soloVictories != this.soloVictories
                || duoVictories != this.duoVictories
                || bestRoboRumbleTime != this.bestRoboRumbleTime
                || bestTimeAsBigBrawler != this.bestTimeAsBigBrawler
                || !Objects.equals(brawlStarsClubTag, this.brawlStarsClubTag);

        if (updated) {
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

        return updated;
    }

    public void deleted() {
        this.status = PlayerStatus.DELETED;
    }

    public void dormantReturned() {
        this.status = PlayerStatus.DORMANT_RETURNED;
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

    public List<PlayerBrawlerCollectionEntity> getBrawlers() {
        return brawlers.stream().toList();
    }

    @Nullable
    public OffsetDateTime getLatestBattleTime() {
        return latestBattleTime;
    }

    @Nullable
    public Integer getSoloRankTier() {
        return soloRankTier;
    }
}
