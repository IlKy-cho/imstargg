create table battle
(
    battle_id                  bigint       not null auto_increment,
    battle_key                 varchar(255) not null,
    battle_time                timestamp(6) not null,
    event_brawlstars_id        bigint,
    event_mode                 varchar(65),
    event_map                  varchar(65),
    mode                       varchar(105) not null,
    type                       varchar(105) not null,
    result                     varchar(25),
    duration                   int,
    star_player_brawlstars_tag varchar(45),
    player_id                  bigint       not null,
    battle_rank                int,
    trophy_change              int,
    trophies_snapshot          int,
    latest                     boolean      not null,
    created_at                 timestamp(6) not null,
    updated_at                 timestamp(6) not null,
    deleted                    boolean      not null,
    primary key (battle_id)
) engine = innodb;

create index ix_battle__playerid_battletime
    on battle (player_id, battle_time desc);

create index ix_battle__eventbrawlstarsid
    on battle (event_brawlstars_id);

create index ix_battle__battletime
    on battle (battle_time desc);


create table battle_player
(
    battle_player_id      bigint       not null auto_increment,
    battle_id             bigint       not null,
    brawlstars_tag        varchar(45)  not null,
    name                  varchar(105) not null,
    team_idx              int          not null,
    idx                   int          not null,
    brawler_brawlstars_id bigint       not null,
    brawler_name          varchar(65)  not null,
    brawler_power         int          not null,
    brawler_trophies      int,
    brawler_trophy_change int,
    created_at            timestamp(6) not null,
    updated_at            timestamp(6) not null,
    deleted               boolean      not null,
    primary key (battle_player_id)
) engine = innodb;

create index ix_battleplayer__battleid
    on battle_player (battle_id);


create table club
(
    club_id           bigint       not null auto_increment,
    brawlstars_tag    varchar(45)  not null,
    name              varchar(105) not null,
    description       varchar(500),
    type              varchar(45),
    badge_id          bigint       not null,
    required_trophies int          not null,
    trophies          int          not null,
    created_at        timestamp(6) not null,
    updated_at        timestamp(6) not null,
    deleted           boolean      not null,
    primary key (club_id)
) engine = innodb;

alter table club
    add constraint uk_club__brawlstarstag unique (brawlstars_tag);

create index ix_club__name
    on club (name);


create table club_member
(
    club_member_id bigint       not null auto_increment,
    club_id        bigint       not null,
    member_id      bigint       not null,
    role           varchar(25)  not null,
    created_at     timestamp(6) not null,
    updated_at     timestamp(6) not null,
    deleted        boolean      not null,
    primary key (club_member_id)
) engine = innodb;

alter table club_member
    add constraint uk_clubmember__clubid_memberid unique (club_id, member_id);


create table player_brawler
(
    player_brawler_id         bigint       not null auto_increment,
    player_id                 bigint       not null,
    brawler_brawlstars_id     bigint       not null,
    power                     int          not null,
    trophy_rank               int          not null,
    trophies                  int          not null,
    highest_trophies          int          not null,
    gear_brawlstars_ids       varchar(255) not null,
    star_power_brawlstars_ids varchar(255) not null,
    gadget_brawlstars_ids     varchar(255) not null,
    created_at                timestamp(6) not null,
    updated_at                timestamp(6) not null,
    deleted                   boolean      not null,
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
    not_updated_count                     int          not null,
    update_weight                         timestamp(6) not null,
    created_at                            timestamp(6) not null,
    updated_at                            timestamp(6) not null,
    deleted                               boolean      not null,
    primary key (player_id)
) engine = innodb;

alter table player
    add constraint uk_player__brawlstarstag unique (brawlstars_tag);

create index ix_player__name
    on player (name);


create table unknown_player
(
    unknown_player_id   bigint       not null auto_increment,
    brawlstars_tag      varchar(45)  not null,
    status              varchar(45)  not null,
    not_found_count     int          not null,
    update_available_at timestamp(6) not null,
    created_at          timestamp(6) not null,
    updated_at          timestamp(6) not null,
    deleted             boolean      not null,
    primary key (unknown_player_id)
) engine = innodb;

alter table unknown_player
    add constraint uk_unknownplayer__brawlstarstag unique (brawlstars_tag);
