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
    updated_at                 timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted                    boolean      not null default false,
    primary key (battle_id)
) engine = innodb;

create index ix_battle__playerid_battletime
    on battle (player_id, battle_time desc);

create index ix_battle__createdat
    on battle (created_at desc);


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
    created_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted           boolean      not null default false,
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
    created_at     timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at     timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted        boolean      not null default false,
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
    created_at                timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                timestamp(6) not null default CURRENT_TIMESTAMP(6),
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
    updated_at                            timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted                               boolean      not null default false,
    primary key (player_id)
) engine = innodb;

alter table player
    add constraint uk_player__brawlstarstag unique (brawlstars_tag);

create index ix_player__name
    on player (name);

create index ix_player__trophies
    on player (trophies);


create table unknown_player
(
    unknown_player_id bigint       not null auto_increment,
    brawlstars_tag    varchar(45)  not null,
    status            varchar(45)  not null,
    not_found_count   int          not null,
    created_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted           boolean      not null default false,
    primary key (unknown_player_id)
) engine = innodb;

alter table unknown_player
    add constraint uk_unknownplayer__brawlstarstag unique (brawlstars_tag);

create index ix_unknownplayer__status
    on unknown_player (status);


create table message
(
    message_id bigint       not null auto_increment,
    code       varchar(105) not null,
    lang       varchar(25)  not null,
    content    text         not null,
    created_at timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) not null default CURRENT_TIMESTAMP(6),
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
    url                 varchar(500) not null,
    created_at          timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at          timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted             boolean      not null default false,
    primary key (brawlstars_image_id)
) engine = innodb;

alter table brawlstars_image
    add constraint uk_brawlstars_image__code unique (code);

-- Statistics

create table brawler_rank
(
    brawler_rank_id       bigint       not null auto_increment,
    battle_event_id       bigint       not null,
    battle_date           date         not null,
    brawler_brawlstars_id bigint       not null,
    created_at            timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at            timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted               boolean      not null default false,
    primary key (brawler_rank_id)
) engine = innodb;


create table brawler_winning
(
    brawler_winning_id          bigint       not null auto_increment,
    battle_event_id             bigint       not null,
    battle_date                 date         not null,
    brawler_brawlstars_id       bigint       not null,
    solo_rank_tier_range        varchar(25)  not null,
    trophy_range                varchar(25)  not null,
    total_count                 int          not null,
    win_count                   int          not null,
    evemy_brawler_brawlstars_id bigint       not null,
    created_at                  timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                  timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted                     boolean      not null default false,
    primary key (brawler_winning_id)
) engine = innodb;


create table brawlers_winning
(
    brawlers_winning_id        bigint       not null auto_increment,
    battle_event_id            bigint       not null,
    battle_date                date         not null,
    brawler_brawlstars_id      bigint       not null,
    solo_rank_tier_range       varchar(25)  not null,
    trophy_range               varchar(25)  not null,
    total_count                int          not null,
    win_count                  int          not null,
    brawler_num                int          not null,
    brawler_brawlstars_id_hash varchar(60)  not null,
    created_at                 timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                 timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted                    boolean      not null default false,
    primary key (brawlers_winning_id)
) engine = innodb;

-- BrawlStars

create table brawlstars_news
(
    brawlstars_news_id bigint       not null auto_increment,
    lang               varchar(25)  not null,
    title              varchar(255) not null,
    link_url           varchar(255) not null,
    publish_date       timestamp(6) not null,
    created_at         timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at         timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted            boolean      not null default false,
    primary key (brawlstars_news_id)
) engine = innodb;

alter table brawlstars_news
    add constraint uk_brawlstars_news__linkurl unique (link_url);

create index ix_brawlstars_news__lang_publishdate
    on brawlstars_news (lang, publish_date desc);

create table battle_event
(
    battle_event_id bigint       not null auto_increment,
    brawlstars_id   bigint       not null,
    mode            varchar(45)  not null,
    map_id          bigint       not null,
    created_at      timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at      timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted         boolean      not null default false,
    primary key (battle_event_id)
) engine = innodb;

alter table battle_event
    add constraint uk_battle_event__brawlstarsid unique (brawlstars_id);


create table seasoned_battle_event
(
    seasoned_battle_event_id bigint       not null auto_increment,
    battle_event_id          bigint       not null,
    created_at               timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at               timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted                  boolean      not null default false,
    primary key (seasoned_battle_event_id)
) engine = innodb;

alter table seasoned_battle_event
    add constraint uk_seasoned_battle_event__battleeventid unique (battle_event_id);


create table battle_map
(
    battle_map_id     bigint       not null auto_increment,
    code              varchar(45)  not null,
    name_message_code varchar(105) not null,
    created_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted           boolean      not null default false,
    primary key (battle_map_id)
) engine = innodb;

alter table battle_map
    add constraint uk_battle_map__code unique (code);


create table brawler
(
    brawler_id        bigint       not null auto_increment,
    brawlstars_id     bigint       not null,
    name_message_code varchar(105) not null,
    rarity            varchar(45)  not null,
    role              varchar(45)  not null,
    created_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted           boolean      not null default false,
    primary key (brawler_id)
) engine = innodb;

alter table brawler
    add constraint uk_brawler__brawlstarsid unique (brawlstars_id);


create table brawler_gear
(
    brawler_gear_id bigint       not null auto_increment,
    brawler_id      bigint       not null,
    gear_id         bigint       not null,
    created_at      timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at      timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted         boolean      not null default false,
    primary key (brawler_gear_id)
) engine = innodb;

alter table brawler_gear
    add constraint uk_brawler_gear__brawlerid_gearid unique (brawler_id, gear_id);


create table gadget
(
    gadget_id         bigint       not null auto_increment,
    brawlstars_id     bigint       not null,
    name_message_code varchar(105) not null,
    brawler_id        bigint       not null,
    created_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted           boolean      not null default false,
    primary key (gadget_id)
) engine = innodb;

alter table gadget
    add constraint uk_gadget__brawlstarsid unique (brawlstars_id);

create table gear
(
    gear_id           bigint       not null auto_increment,
    brawlstars_id     bigint       not null,
    name_message_code varchar(105) not null,
    rarity            varchar(45)  not null,
    created_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted           boolean      not null default false,
    primary key (gear_id)
) engine = innodb;

alter table gear
    add constraint uk_gear__brawlstarsid unique (brawlstars_id);

create table star_power
(
    star_power_id     bigint       not null auto_increment,
    brawlstars_id     bigint       not null,
    name_message_code varchar(105) not null,
    brawler_id        bigint       not null,
    created_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at        timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted           boolean      not null default false,
    primary key (star_power_id)
) engine = innodb;

alter table star_power
    add constraint uk_starpower__brawlstarsid unique (brawlstars_id);


-- Collection

create table unregistered_battle_event
(
    unregistered_battle_event_id bigint       not null auto_increment,
    battle_id                    bigint       not null,
    event_brawlstars_id          bigint,
    event_mode                   varchar(65),
    event_map                    varchar(65),
    created_at                   timestamp(6) not null default CURRENT_TIMESTAMP(6),
    updated_at                   timestamp(6) not null default CURRENT_TIMESTAMP(6),
    deleted                      boolean      not null default false,
    primary key (unregistered_battle_event_id)
) engine = innodb;

alter table unregistered_battle_event
    add constraint uk_unregistered_battle_event__eventbrawlstarsid unique (event_brawlstars_id);
