import {BattleMode, BattleModeValue} from "@/model/enums/BattleMode";
import {BattleEventMode, BattleEventModeValue} from "@/model/enums/BattleEventMode";
import {PlayerBattle} from "@/model/PlayerBattle";
import {BattleType, BattleTypeValue} from "@/model/enums/BattleType";

export const BrawlStarsIconSrc = {
  CLUB_LEAGUE_MASTERS: '/icon/icon_club_league_masters.png',
  FAMILY_FRIENDLY: '/icon/icon_family_friendly.png',
  RANKED_FRONT: '/icon/icon_ranked_front.png',
  TROPHY: '/icon/icon_trophy.png',
} as const;

const ModeIconSrc = {
  GODZILLA_CITY_SMASH: '/mode/고질라 시티 스매시.webp',
  KNOCKOUT: '/mode/녹아웃.webp',
  DUELS: '/mode/듀얼.webp',
  DUO_SHOWDOWN: '/mode/듀오 쇼다운.webp',
  ROBO_RUMBLE: '/mode/로보럼블.webp',
  LONE_STAR: '/mode/론스타.webp',
  BASKET_BRAWL: '/mode/바스켓 브롤.webp',
  BOUNTY: '/mode/바운티.webp',
  VOLLEY_BRAWL: '/mode/발리 브롤.webp',
  BOSS_FIGHT: '/mode/보스전.webp',
  BOT_DROP: '/mode/봇드롭.webp',
  BRAWL_BALL: '/mode/브롤볼.webp',
  BIG_GAME: '/mode/빅게임.webp',
  PRESENT_PLUNDER: '/mode/선물 훔치기.webp',
  SOLO_SHOWDOWN: '/mode/솔로 쇼다운.webp',
  SUPER_CITY_RAMPAGE: '/mode/슈퍼시티 램피지.webp',
  SIEGE: '/mode/시즈 팩토리.webp',
  GEM_GRAB: '/mode/젬그랩.webp',
  LAST_STAND: '/mode/최후의 저항.webp',
  WIPEOUT: '/mode/클린 아웃.webp',
  TAKEDOWN: '/mode/테이크다운.webp',
  TROPHY_THIEVES: '/mode/트로피를 도둑.webp',
  HOLD_THE_TROPHY: '/mode/트로피를 잡아라.webp',
  PAYLOAD: '/mode/페이로드.webp',
  HEIST: '/mode/하이스트.webp',
  HOT_ZONE: '/mode/핫존.webp',
  JELLYFISHING: '/mode/해파리사냥.webp',
  HUNTERS: '/mode/헌터스.webp',
  TRIO_SHOWDOWN: '/mode/트리오 쇼다운.webp',
} as const;

export const battleModeIconSrc = (mode: BattleMode) => {
  switch (mode) {
    case BattleModeValue.NOT_FOUND:
      return null;
    case BattleModeValue.KNOCKOUT:
      return ModeIconSrc.KNOCKOUT;
    case BattleModeValue.GEM_GRAB:
      return ModeIconSrc.GEM_GRAB;
    case BattleModeValue.HEIST:
      return ModeIconSrc.HEIST;
    case BattleModeValue.HOT_ZONE:
      return ModeIconSrc.HOT_ZONE;
    case BattleModeValue.SIEGE:
      return ModeIconSrc.SIEGE;
    case BattleModeValue.BOUNTY:
      return ModeIconSrc.BOUNTY;
    case BattleModeValue.BRAWL_BALL:
      return ModeIconSrc.BRAWL_BALL;
    case BattleModeValue.DUELS:
      return ModeIconSrc.DUELS;
    case BattleModeValue.SOLO_SHOWDOWN:
      return ModeIconSrc.SOLO_SHOWDOWN;
    case BattleModeValue.DUO_SHOWDOWN:
      return ModeIconSrc.DUO_SHOWDOWN;
    case BattleModeValue.WIPEOUT:
      return ModeIconSrc.WIPEOUT;
    case BattleModeValue.VOLLEY_BRAWL:
      return ModeIconSrc.VOLLEY_BRAWL;
    case BattleModeValue.TROPHY_THIEVES:
      return ModeIconSrc.TROPHY_THIEVES;
    case BattleModeValue.BOSS_FIGHT:
      return ModeIconSrc.BOSS_FIGHT;
    case BattleModeValue.BIG_GAME:
      return ModeIconSrc.BIG_GAME;
    case BattleModeValue.BASKET_BRAWL:
      return ModeIconSrc.BASKET_BRAWL;
    case BattleModeValue.ROBO_RUMBLE:
      return ModeIconSrc.ROBO_RUMBLE;
    case BattleModeValue.PAYLOAD:
      return ModeIconSrc.PAYLOAD;
    case BattleModeValue.TAKEDOWN:
      return ModeIconSrc.TAKEDOWN;
    case BattleModeValue.HUNTERS:
      return ModeIconSrc.HUNTERS;
  }
}

export const battleEventModeIconSrc = (mode: BattleEventMode) => {
  switch (mode) {
    case BattleEventModeValue.NOT_FOUND:
      return null;
    case BattleEventModeValue.UNKNOWN:
      return null;
    case BattleEventModeValue.SOLO_SHOWDOWN:
      return ModeIconSrc.SOLO_SHOWDOWN;
    case BattleEventModeValue.DUO_SHOWDOWN:
      return ModeIconSrc.DUO_SHOWDOWN;
    case BattleEventModeValue.TRIO_SHOWDOWN:
      return ModeIconSrc.TRIO_SHOWDOWN;
    case BattleEventModeValue.BASKET_BRAWL:
      return ModeIconSrc.BASKET_BRAWL;
    case BattleEventModeValue.BIG_GAME:
      return ModeIconSrc.BIG_GAME;
    case BattleEventModeValue.BOSS_FIGHT:
      return ModeIconSrc.BOSS_FIGHT;
    case BattleEventModeValue.BOT_DROP:
      return ModeIconSrc.BOT_DROP;
    case BattleEventModeValue.BOUNTY:
      return ModeIconSrc.BOUNTY;
    case BattleEventModeValue.BRAWL_BALL:
      return ModeIconSrc.BRAWL_BALL;
    case BattleEventModeValue.BRAWL_BALL_5V5:
      return ModeIconSrc.BRAWL_BALL;
    case BattleEventModeValue.DUELS:
      return ModeIconSrc.DUELS;
    case BattleEventModeValue.GEM_GRAB:
      return ModeIconSrc.GEM_GRAB;
    case BattleEventModeValue.GEM_GRAB_5V5:
      return ModeIconSrc.GEM_GRAB;
    case BattleEventModeValue.HOT_ZONE:
      return ModeIconSrc.HOT_ZONE;
    case BattleEventModeValue.HUNTERS:
      return ModeIconSrc.HUNTERS;
    case BattleEventModeValue.HEIST:
      return ModeIconSrc.HEIST;
    case BattleEventModeValue.JELLYFISHING:
      return ModeIconSrc.JELLYFISHING;
    case BattleEventModeValue.KNOCKOUT:
      return ModeIconSrc.KNOCKOUT;
    case BattleEventModeValue.KNOCKOUT_5V5:
      return ModeIconSrc.KNOCKOUT;
    case BattleEventModeValue.PAINT_BRAWL:
      return null;
    case BattleEventModeValue.PAYLOAD:
      return ModeIconSrc.PAYLOAD;
    case BattleEventModeValue.PRESENT_PLUNDER:
      return ModeIconSrc.PRESENT_PLUNDER;
    case BattleEventModeValue.ROBO_RUMBLE:
      return ModeIconSrc.ROBO_RUMBLE;
    case BattleEventModeValue.TAKEDOWN:
      return ModeIconSrc.TAKEDOWN;
    case BattleEventModeValue.TROPHY_ESCAPE:
      return null;
    case BattleEventModeValue.VOLLEY_BRAWL:
      return ModeIconSrc.VOLLEY_BRAWL;
    case BattleEventModeValue.WIPEOUT:
      return ModeIconSrc.WIPEOUT;
    case BattleEventModeValue.WIPEOUT_5V5:
      return ModeIconSrc.WIPEOUT;
    case BattleEventModeValue.LONE_STAR:
      return ModeIconSrc.LONE_STAR;
    case BattleEventModeValue.SUPER_CITY_RAMPAGE:
      return ModeIconSrc.SUPER_CITY_RAMPAGE;
    case BattleEventModeValue.HOLD_THE_TROPHY:
      return ModeIconSrc.HOLD_THE_TROPHY;
    case BattleEventModeValue.TROPHY_THIEVES:
      return ModeIconSrc.TROPHY_THIEVES;
    case BattleEventModeValue.LAST_STAND:
      return ModeIconSrc.LAST_STAND;
    case BattleEventModeValue.SNOWTEL_THIEVES:
      return null;
    case BattleEventModeValue.PUMPKIN_PLUNDER:
      return null;
    case BattleEventModeValue.GODZILLA_CITY_SMASH:
      return ModeIconSrc.GODZILLA_CITY_SMASH;
    case BattleEventModeValue.ZOMBIE_PLUNDER:
      return null;
  }
}

export const playerBattleIconSrc = (battle: PlayerBattle)=> {
  const eventModeIcon = battle.event ? battleEventModeIconSrc(battle.event.mode) : null;
  return eventModeIcon || battleModeIconSrc(battle.mode);
}

export const battleTypeIconSrc = (type: BattleType) => {
  switch (type) {
    case BattleTypeValue.NOT_FOUND:
      return null;
    case BattleTypeValue.RANKED:
      return BrawlStarsIconSrc.TROPHY;
    case BattleTypeValue.SOLO_RANKED:
      return BrawlStarsIconSrc.RANKED_FRONT;
    case BattleTypeValue.FRIENDLY:
      return BrawlStarsIconSrc.FAMILY_FRIENDLY;
    case BattleTypeValue.CHALLENGE:
      return null;
  }
}
