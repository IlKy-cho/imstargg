create table battle
(
    battle_id                  bigint       not null auto_increment,
    battle_key                 varchar(255) not null,
    battle_time                timestamp(6) not null,
    event_id                   bigint       not null,
    mode                       varchar(105) not null,
    type                       varchar(105) not null,
    result                     varchar(25)  not null,
    duration                   int          not null,
    star_player_brawlstars_tag varchar(45),
    player_id                  bigint       not null,
    brawler_id                 bigint       not null,
    power                      int          not null,
    brawler_trophies           int,
    trophy_change              int,
    trophies_snapshot          int,
    brawler_trophies_snapshot  int,
    created_at                 timestamp(6) not null,
    updated_at                 timestamp(6) not null,
    deleted                    boolean      not null,
    primary key (battle_id)
) engine = innodb;

create index ix_battle__eventid
    on battle (event_id);

create index ix_battle__createdat
    on battle (created_at desc);


create table battle_event
(
    battle_event_id bigint       not null auto_increment,
    brawlstars_id   bigint       not null,
    mode            varchar(105) not null,
    map             varchar(105) not null,
    created_at      timestamp(6) not null,
    updated_at      timestamp(6) not null,
    deleted         boolean      not null,
    primary key (battle_event_id)
) engine = innodb;

alter table battle_event
    add constraint uk_battleevent__brawlstarsid unique (brawlstars_id);


create table battle_player
(
    battle_player_id bigint       not null auto_increment,
    battle_id        bigint       not null,
    brawlstars_tag   varchar(45)  not null,
    brawler_id       bigint       not null,
    power            int          not null,
    trophies         int,
    team_idx         int          not null,
    idx              int          not null,
    created_at       timestamp(6) not null,
    updated_at       timestamp(6) not null,
    deleted          boolean      not null,
    primary key (battle_player_id)
) engine = innodb;


create table brwaler
(
    brawler_id     bigint       not null auto_increment,
    brawlstars_id  bigint       not null,
    name           varchar(105) not null,
    rarity         varchar(45),
    role           varchar(45),
    gear_ids       varchar(255) not null,
    star_power_ids varchar(255) not null,
    gadget_ids     varchar(255) not null,
    created_at     timestamp(6) not null,
    updated_at     timestamp(6) not null,
    deleted        boolean      not null,
    primary key (brawler_id)
) engine = innodb;

alter table brwaler
    add constraint uk_brawler__brawlstarsid unique (brawlstars_id);


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


create table gadget
(
    gadget_id     bigint       not null auto_increment,
    brawlstars_id bigint       not null,
    name          varchar(105) not null,
    created_at    timestamp(6) not null,
    updated_at    timestamp(6) not null,
    deleted       boolean      not null,
    primary key (gadget_id)
) engine = innodb;

alter table gadget
    add constraint uk_gadget__brawlstarsid unique (brawlstars_id);


create table gear
(
    gear_id       bigint       not null auto_increment,
    brawlstars_id bigint       not null,
    name          varchar(105) not null,
    level         int,
    rarity        varchar(45),
    created_at    timestamp(6) not null,
    updated_at    timestamp(6) not null,
    deleted       boolean      not null,
    primary key (gear_id)
) engine = innodb;

alter table star_power
    add constraint uk_gear__brawlstarsid unique (brawlstars_id);


create table illegal_player
(
    illegal_player_id bigint       not null auto_increment,
    brawlstars_tag    varchar(45)  not null,
    count             int          not null,
    available_at      timestamp(6) not null,
    created_at        timestamp(6) not null,
    updated_at        timestamp(6) not null,
    deleted           boolean      not null,
    primary key (illegal_player_id)
) engine = innodb;


create table player_brawler
(
    player_brawler_id bigint       not null auto_increment,
    player_id         bigint       not null,
    brawler_id        bigint       not null,
    power             int          not null,
    trophy_rank       int          not null,
    trophies          int          not null,
    highest_trophies  int          not null,
    gear_ids          varchar(255) not null,
    star_power_ids    varchar(255) not null,
    gadget_ids        varchar(255) not null,
    created_at        timestamp(6) not null,
    updated_at        timestamp(6) not null,
    deleted           boolean      not null,
    primary key (player_brawler_id)
) engine = innodb;

alter table player_brawler
    add constraint uk_playerbrawler_playerid_brawlerid unique (player_id, brawler_id);


create table player
(
    player_id                             bigint       not null auto_increment,
    brawlstars_tag                        varchar(45)  not null,
    status                                varchar(45)  not null,
    name                                  varchar(105) not null,
    name_color                            varchar(45)  not null,
    icon_id                               bigint       not null,
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
    brawlstars_club_tag                   varchar(45)  not null,
    update_weight                         bigint       not null,
    created_at                            timestamp(6) not null,
    updated_at                            timestamp(6) not null,
    deleted                               boolean      not null,
    primary key (player_id)
) engine = innodb;

alter table player
    add constraint uk_player__brawlstarstag unique (brawlstars_tag);

create index ix_player__name
    on player (name);


create table star_power
(
    star_power_id bigint       not null auto_increment,
    brawlstars_id bigint       not null,
    name          varchar(105) not null,
    created_at    timestamp(6) not null,
    updated_at    timestamp(6) not null,
    deleted       boolean      not null,
    primary key (star_power_id)
) engine = innodb;

alter table star_power
    add constraint uk_starpower__brawlstarsid unique (brawlstars_id);
