create table battle
(
    battle_id                  bigint       not null auto_increment,
    battle_key                 varchar(255) not null,
    battle_time                timestamp(6) not null,
    event_brawlstars_id        bigint,
    event_mode                 varchar(65),
    event_map                  varchar(65),
    mode                       varchar(105) not null,
    type                       varchar(105),
    result                     varchar(25),
    duration                   int,
    star_player_brawlstars_tag varchar(45),
    player_id                  bigint       not null,
    battle_rank                int,
    trophy_change              int,
    teams                      json         not null,
    created_at                 timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                 timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted                    boolean      not null default false,
    primary key (battle_id)
) engine = innodb;

create index ix_battle__battletime
    on battle (battle_time desc);

create index ix_battle__playerid_battletime
    on battle (player_id, battle_time desc);

create index ix_event_type_battletime
    on battle (event_brawlstars_id, type, battle_time desc);


create table club
(
    club_id             bigint       not null auto_increment,
    brawlstars_tag      varchar(45)  not null,
    status              varchar(45)  not null,
    name                varchar(105) not null,
    description         varchar(500),
    type                varchar(45)  not null,
    badge_brawlstars_id bigint       not null,
    required_trophies   int          not null,
    trophies            int          not null,
    created_at          timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at          timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted             boolean      not null default false,
    primary key (club_id)
) engine = innodb;

alter table club
    add constraint uk_club__brawlstarstag unique (brawlstars_tag);

create index ix_club__name
    on club (name);


create table club_member
(
    club_member_id     bigint       not null auto_increment,
    club_id            bigint       not null,
    brawlstars_tag     varchar(45)  not null,
    name               varchar(45)  not null,
    name_color         varchar(45),
    role               varchar(25)  not null,
    trophies           int          not null,
    icon_brawlstars_id bigint       not null,
    created_at         timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at         timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted            boolean      not null default false,
    primary key (club_member_id)
) engine = innodb;

alter table club_member
    add constraint uk_clubmember__clubid_brawlstarstag unique (club_id, brawlstars_tag);


create table player_brawler
(
    player_brawler_id         bigint       not null auto_increment,
    player_id                 bigint       not null,
    brawler_brawlstars_id     bigint       not null,
    power                     int          not null,
    trophy_rank               int          not null,
    trophies                  int          not null,
    highest_trophies          int          not null,
    gear_brawlstars_ids       json         not null,
    star_power_brawlstars_ids json         not null,
    gadget_brawlstars_ids     json         not null,
    created_at                timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted                   boolean      not null default false,
    primary key (player_brawler_id)
) engine = innodb;

alter table player_brawler
    add constraint uk_playerbrawler__playerid_brawlerbrawlstarsid unique (player_id, brawler_brawlstars_id);


create table player
(
    player_id                             bigint       not null auto_increment,
    brawlstars_tag                        varchar(45)  not null,
    status                                varchar(45)  not null,
    name                                  varchar(105) not null,
    name_color                            varchar(45),
    icon_brawlstars_id                    bigint       not null,
    trophies                              int          not null,
    highest_trophies                      int          not null,
    exp_level                             int          not null,
    exp_points                            int          not null,
    qualified_from_championship_challenge boolean      not null,
    victories_3vs3                        int          not null,
    solo_victories                        int          not null,
    duo_victories                         int          not null,
    best_robo_rumble_time                 int          not null,
    best_time_as_big_brawler              int          not null,
    brawlstars_club_tag                   varchar(45),
    latest_battle_time                    timestamp(6),
    solo_rank_tier                        int,
    version                               int          not null,
    created_at                            timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                            timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted                               boolean      not null default false,
    primary key (player_id)
) engine = innodb;

alter table player
    add constraint uk_player__brawlstarstag unique (brawlstars_tag);

create index ix_player__trophies
    on player (trophies);


create table unknown_player
(
    unknown_player_id bigint       not null auto_increment,
    brawlstars_tag    varchar(45)  not null,
    not_found_count   int          not null,
    version           int          not null,
    created_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at        timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted           boolean      not null default false,
    primary key (unknown_player_id)
) engine = innodb;

alter table unknown_player
    add constraint uk_unknownplayer__brawlstarstag unique (brawlstars_tag);


create table player_renewal
(
    player_renewal_id bigint       not null auto_increment,
    brawlstars_tag    varchar(45)  not null,
    status            varchar(25)  not null,
    version           int          not null,
    created_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at        timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted           boolean      not null default false,
    primary key (player_renewal_id)
) engine = innodb;

alter table player_renewal
    add constraint uk_brawlstarstag unique (brawlstars_tag);

create index ix_status
    on player_renewal (status);


create table message
(
    message_id bigint       not null auto_increment,
    code       varchar(105) not null,
    lang       varchar(25)  not null,
    content    text         not null,
    created_at timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted    boolean      not null default false,
    primary key (message_id)
) engine = innodb;

alter table message
    add constraint uk_message__code_lang unique (code, lang);


create table brawlstars_image
(
    brawlstars_image_id bigint       not null auto_increment,
    type                varchar(25)  not null,
    code                varchar(65)  not null,
    stored_name         varchar(255) not null,
    created_at          timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at          timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted             boolean      not null default false,
    primary key (brawlstars_image_id)
) engine = innodb;

alter table brawlstars_image
    add constraint uk_brawlstars_image__code unique (code);

-- Statistics

create table brawler_battle_rank_stats_v3
(
    brawler_battle_rank_stats_id bigint       not null auto_increment,
    event_brawlstars_id          bigint       not null,
    brawler_brawlstars_id        bigint       not null,
    tier_range                   varchar(25),
    battle_date                  date         not null,
    rank_to_counts               json         not null,
    created_at                   timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                   timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted                      boolean      not null default false,
    primary key (brawler_battle_rank_stats_id)
) engine = innodb;

alter table brawler_battle_rank_stats_v3
    add constraint uk_brawler_battle_rank_stats__key
        unique (event_brawlstars_id, brawler_brawlstars_id, tier_range, battle_date);

create index ix_brawler_battle_rank_stats__1
    on brawler_battle_rank_stats_v3 (battle_date desc);


create table brawler_battle_result_stats_v3
(
    brawler_battle_result_stats_id bigint       not null auto_increment,
    event_brawlstars_id            bigint       not null,
    brawler_brawlstars_id          bigint       not null,
    tier_range                     varchar(25),
    battle_date                    date         not null,
    victory_count                  bigint       not null,
    defeat_count                   bigint       not null,
    draw_count                     bigint       not null,
    star_player_count              bigint       not null,
    created_at                     timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                     timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted                        boolean      not null default false,
    primary key (brawler_battle_result_stats_id)
) engine = innodb;

alter table brawler_battle_result_stats_v3
    add constraint uk_brawler_battle_result_stats__key
        unique (event_brawlstars_id, brawler_brawlstars_id, tier_range, battle_date);

create index ix_brawler_battle_result_stats__1
    on brawler_battle_result_stats_v3 (battle_date desc);

create index ix_brawler_battle_result_stats__2
    on brawler_battle_result_stats_v3 (tier_range, battle_date desc);

create index ix_brawler_battle_result_stats__3
    on brawler_battle_result_stats_v3 (brawler_brawlstars_id, tier_range, battle_date desc);


create table brawler_enemy_battle_result_stats_v3
(
    brawler_enemy_battle_result_stats_id bigint       not null auto_increment,
    event_brawlstars_id                  bigint       not null,
    brawler_brawlstars_id                bigint       not null,
    enemy_brawler_brawlstars_id          bigint       not null,
    tier_range                           varchar(25),
    battle_date                          date         not null,
    victory_count                        bigint       not null,
    defeat_count                         bigint       not null,
    draw_count                           bigint       not null,
    created_at                           timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                           timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted                              boolean      not null default false,
    primary key (brawler_enemy_battle_result_stats_id)
) engine = innodb;

alter table brawler_enemy_battle_result_stats_v3
    add constraint uk_brawler_enemy_battle_result_stats__key
        unique (event_brawlstars_id, brawler_brawlstars_id, tier_range, battle_date, enemy_brawler_brawlstars_id);

create index ix_brawler_battle_result_stats__1
    on brawler_enemy_battle_result_stats_v3 (battle_date desc);

create index ix_brawler_battle_result_stats__2
    on brawler_enemy_battle_result_stats_v3 (brawler_brawlstars_id, tier_range, battle_date desc);


create table brawler_pair_battle_rank_stats_v3
(
    brawler_pair_battle_rank_stats_id bigint       not null auto_increment,
    event_brawlstars_id               bigint       not null,
    brawler_brawlstars_id             bigint       not null,
    pair_brawler_brawlstars_id        bigint       not null,
    tier_range                        varchar(25),
    battle_date                       date         not null,
    rank_to_counts                    json         not null,
    created_at                        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                        timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted                           boolean      not null default false,
    primary key (brawler_pair_battle_rank_stats_id)
) engine = innodb;

alter table brawler_pair_battle_rank_stats_v3
    add constraint uk_brawler_pair_battle_rank_stats__key
        unique (event_brawlstars_id, brawler_brawlstars_id, tier_range, battle_date, pair_brawler_brawlstars_id);

create index ix_brawler_battle_rank_stats__1
    on brawler_pair_battle_rank_stats_v3 (battle_date desc);


create table brawler_pair_battle_result_stats_v3
(
    brawler_pair_battle_result_stats_id bigint       not null auto_increment,
    event_brawlstars_id                 bigint       not null,
    brawler_brawlstars_id               bigint       not null,
    pair_brawler_brawlstars_id          bigint       not null,
    tier_range                          varchar(25),
    battle_date                         date         not null,
    victory_count                       bigint       not null,
    defeat_count                        bigint       not null,
    draw_count                          bigint       not null,
    created_at                          timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                          timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted                             boolean      not null default false,
    primary key (brawler_pair_battle_result_stats_id)
) engine = innodb;

alter table brawler_pair_battle_result_stats_v3
    add constraint uk_brawler_pair_battle_result_stats__key
        unique (event_brawlstars_id, brawler_brawlstars_id, tier_range, battle_date, pair_brawler_brawlstars_id);

create index ix_brawlers_battle_result_stats__1
    on brawler_pair_battle_result_stats_v3 (battle_date desc);

create index ix_brawlers_battle_result_stats__2
    on brawler_pair_battle_result_stats_v3 (brawler_brawlstars_id, tier_range, battle_date desc);


create table brawler_count_v3
(
    brawler_count_id      bigint       not null auto_increment,
    brawler_brawlstars_id bigint       not null,
    trophy_range          varchar(25)  not null,
    count_value           int          not null,
    created_at            timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at            timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted               boolean      not null default false,
    primary key (brawler_count_id)
);

alter table brawler_count_v3
    add constraint uk_brawler_count__key
        unique (brawler_brawlstars_id, trophy_range);


create table brawler_item_count_v3
(
    brawler_item_count_id bigint       not null auto_increment,
    brawler_brawlstars_id bigint       not null,
    item_brawlstars_id    bigint       not null,
    trophy_range          varchar(25)  not null,
    count_value           int          not null,
    created_at            timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at            timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted               boolean      not null default false,
    primary key (brawler_item_count_id)
);

alter table brawler_item_count_v3
    add constraint uk_brawler_item_count__key
        unique (brawler_brawlstars_id, item_brawlstars_id, trophy_range);


create table stats_collected_battle
(
    stats_collected_battle_id bigint       not null auto_increment,
    stats_key                 varchar(255) not null,
    last_collected_id         bigint       not null,
    created_at                timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted                   boolean      not null default false,
    primary key (stats_collected_battle_id)
) engine = innodb;

alter table stats_collected_battle
    add constraint uk_stats_collected_battle__key unique (stats_key);


-- BrawlStars
create table battle_event
(
    battle_event_id     bigint       not null auto_increment,
    brawlstars_id       bigint       not null,
    mode                varchar(45)  not null,
    map_brawlstars_name varchar(105),
    solo_ranked         boolean      not null default false,
    created_at          timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at          timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted             boolean      not null default false,
    primary key (battle_event_id)
) engine = innodb;

alter table battle_event
    add constraint uk_brawlstarsid unique (brawlstars_id);


create table brawler
(
    brawler_id        bigint       not null auto_increment,
    brawlstars_id     bigint       not null,
    name_message_code varchar(105) not null,
    rarity            varchar(45)  not null,
    role              varchar(45)  not null,
    created_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at        timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted           boolean      not null default false,
    primary key (brawler_id)
) engine = innodb;

alter table brawler
    add constraint uk_brawler__brawlstarsid unique (brawlstars_id);


create table brawler_gear
(
    brawler_gear_id       bigint       not null auto_increment,
    brawler_brawlstars_id bigint       not null,
    gear_brawlstars_id    bigint       not null,
    created_at            timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at            timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted               boolean      not null default false,
    primary key (brawler_gear_id)
) engine = innodb;

alter table brawler_gear
    add constraint uk_brawler_gear unique (brawler_brawlstars_id, gear_brawlstars_id);


create table gadget
(
    gadget_id             bigint       not null auto_increment,
    brawlstars_id         bigint       not null,
    name_message_code     varchar(105) not null,
    brawler_brawlstars_id bigint       not null,
    created_at            timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at            timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted               boolean      not null default false,
    primary key (gadget_id)
) engine = innodb;

alter table gadget
    add constraint uk_brawlstarsid unique (brawlstars_id);

create table gear
(
    gear_id           bigint       not null auto_increment,
    brawlstars_id     bigint       not null,
    name_message_code varchar(105) not null,
    rarity            varchar(45)  not null,
    created_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at        timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted           boolean      not null default false,
    primary key (gear_id)
) engine = innodb;

alter table gear
    add constraint uk_brawlstarsid unique (brawlstars_id);

create table star_power
(
    star_power_id         bigint       not null auto_increment,
    brawlstars_id         bigint       not null,
    name_message_code     varchar(105) not null,
    brawler_brawlstars_id bigint       not null,
    created_at            timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at            timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted               boolean      not null default false,
    primary key (star_power_id)
) engine = innodb;

alter table star_power
    add constraint uk_brawlstarsid unique (brawlstars_id);


create table battle_event_rotation
(
    battle_event_rotation_id bigint       not null auto_increment,
    created_at               timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at               timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted                  boolean      not null default false,
    primary key (battle_event_rotation_id)
) engine = innodb;

create table battle_event_rotation_item
(
    battle_event_rotation_item_id bigint       not null auto_increment,
    battle_event_rotation_id      bigint       not null,
    event_brawlstars_id           bigint       not null,
    modifiers                     json         not null,
    slot_id                       bigint       not null,
    start_time                    timestamp    not null,
    end_time                      timestamp    not null,
    created_at                    timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                    timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted                       boolean      not null default false,
    primary key (battle_event_rotation_item_id)
) engine = innodb;

create index ix_battle_event_rotation_item__1 on battle_event_rotation_item (battle_event_rotation_id);


create table solo_rank_battle_event
(
    solo_rank_battle_event_id bigint       not null auto_increment,
    event_brawlstars_id       bigint       not null,
    created_at                timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted                   boolean      not null default false,
    primary key (solo_rank_battle_event_id)
) engine = innodb;

alter table solo_rank_battle_event
    add constraint uk_solo_rank_battle_event__key unique (event_brawlstars_id);


create table solo_rank_season
(
    solo_rank_season_id bigint       not null auto_increment primary key,
    season_number       int          not null,
    season_month        int          not null,
    start_at            timestamp    not null,
    end_at              timestamp    not null,
    created_at          timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at          timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted             boolean      not null default false
);

alter table solo_rank_season
    add constraint uk_solo_rank_season__key unique (season_number, season_month);


-- ranking
create table player_ranking
(
    player_ranking_id         bigint       not null auto_increment,
    country                   varchar(25)  not null,
    player_brawlstars_tag     varchar(45)  not null,
    player_name               varchar(105) not null,
    player_name_color         varchar(45),
    player_icon_brawlstars_id bigint       not null,
    player_club_name          varchar(105),
    trophies                  int          not null,
    rank_value                int          not null,
    rank_change               int,
    created_at                timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted                   boolean      not null default false,
    primary key (player_ranking_id)
) engine = innodb;

alter table player_ranking
    add constraint uk_player_ranking__country_rank unique (country, rank_value);


create table brawler_ranking
(
    brawler_ranking_id        bigint       not null auto_increment,
    country                   varchar(25)  not null,
    brawler_brawlstars_id     bigint       not null,
    player_brawlstars_tag     varchar(45)  not null,
    player_name               varchar(105) not null,
    player_name_color         varchar(45),
    player_icon_brawlstars_id bigint       not null,
    player_club_name          varchar(105),
    trophies                  int          not null,
    rank_value                int          not null,
    rank_change               int,
    created_at                timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted                   boolean      not null default false,
    primary key (brawler_ranking_id)
) engine = innodb;

alter table brawler_ranking
    add constraint uk_brawler_ranking__country_brawler_rank unique (country, brawler_brawlstars_id, rank_value);


create table club_ranking
(
    club_ranking_id     bigint       not null auto_increment,
    country             varchar(25)  not null,
    brawlstars_tag      varchar(45)  not null,
    name                varchar(45)  not null,
    badge_brawlstars_id bigint       not null,
    trophies            int          not null,
    rank_value          int          not null,
    member_count        int          not null,
    created_at          timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at          timestamp(6) not null default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
    deleted             boolean      not null default false,
    primary key (club_ranking_id)
) engine = innodb;

alter table club_ranking
    add constraint uk_club_ranking__country_rank unique (country, rank_value);

