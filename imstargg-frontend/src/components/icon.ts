import {SoloRankTier, SoloRankTierType} from '@/model/enums/SoloRankTier';
import {BattleMode, BattleModeType} from "@/model/enums/BattleMode";
import {BattleEventMode, BattleEventModeType} from "@/model/enums/BattleEventMode";
import {PlayerBattle} from "@/model/PlayerBattle";

export const BrawlStarsIconSrc = {
  TROPHY: '/icon_trophy.png',
} as const;

export const soloRankTierIconSrc = (tier: SoloRankTierType) => {
  switch (tier) {
    case SoloRankTier.BRONZE_1:
    case SoloRankTier.BRONZE_2:
    case SoloRankTier.BRONZE_3:
      return '/icon_ranked_bronze.png';
    case SoloRankTier.SILVER_1:
    case SoloRankTier.SILVER_2:
    case SoloRankTier.SILVER_3:
      return '/icon_ranked_silver.png';
    case SoloRankTier.GOLD_1:
    case SoloRankTier.GOLD_2:
    case SoloRankTier.GOLD_3:
      return '/icon_ranked_gold.png';
    case SoloRankTier.DIAMOND_1:
    case SoloRankTier.DIAMOND_2:
    case SoloRankTier.DIAMOND_3:
      return '/icon_ranked_diamond.png';
    case SoloRankTier.MYTHIC_1:
    case SoloRankTier.MYTHIC_2:
    case SoloRankTier.MYTHIC_3:
      return '/icon_ranked_mythic.png';
    case SoloRankTier.LEGENDARY_1:
    case SoloRankTier.LEGENDARY_2:
    case SoloRankTier.LEGENDARY_3:
      return '/icon_ranked_legendary.png';
    case SoloRankTier.MASTER:
      return '/icon_ranked_masters.png';
  }
}

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

export const battleModeIconSrc = (mode: BattleModeType) => {
  switch (mode) {
    case BattleMode.NOT_FOUND:
      return null;
    case BattleMode.KNOCKOUT:
      return ModeIconSrc.KNOCKOUT;
    case BattleMode.GEM_GRAB:
      return ModeIconSrc.GEM_GRAB;
    case BattleMode.HEIST:
      return ModeIconSrc.HEIST;
    case BattleMode.HOT_ZONE:
      return ModeIconSrc.HOT_ZONE;
    case BattleMode.SIEGE:
      return ModeIconSrc.SIEGE;
    case BattleMode.BOUNTY:
      return ModeIconSrc.BOUNTY;
    case BattleMode.BRAWL_BALL:
      return ModeIconSrc.BRAWL_BALL;
    case BattleMode.DUELS:
      return ModeIconSrc.DUELS;
    case BattleMode.SOLO_SHOWDOWN:
      return ModeIconSrc.SOLO_SHOWDOWN;
    case BattleMode.DUO_SHOWDOWN:
      return ModeIconSrc.DUO_SHOWDOWN;
    case BattleMode.WIPEOUT:
      return ModeIconSrc.WIPEOUT;
    case BattleMode.VOLLEY_BRAWL:
      return ModeIconSrc.VOLLEY_BRAWL;
    case BattleMode.TROPHY_THIEVES:
      return ModeIconSrc.TROPHY_THIEVES;
    case BattleMode.BOSS_FIGHT:
      return ModeIconSrc.BOSS_FIGHT;
    case BattleMode.BIG_GAME:
      return ModeIconSrc.BIG_GAME;
    case BattleMode.BASKET_BRAWL:
      return ModeIconSrc.BASKET_BRAWL;
    case BattleMode.ROBO_RUMBLE:
      return ModeIconSrc.ROBO_RUMBLE;
    case BattleMode.PAYLOAD:
      return ModeIconSrc.PAYLOAD;
    case BattleMode.TAKEDOWN:
      return ModeIconSrc.TAKEDOWN;
    case BattleMode.HUNTERS:
      return ModeIconSrc.HUNTERS;
  }
}

export const battleEventModeIconSrc = (mode: BattleEventModeType) => {
  switch (mode) {
    case BattleEventMode.NOT_FOUND:
      return null;
    case BattleEventMode.UNKNOWN:
      return null;
    case BattleEventMode.SOLO_SHOWDOWN:
      return ModeIconSrc.SOLO_SHOWDOWN;
    case BattleEventMode.DUO_SHOWDOWN:
      return ModeIconSrc.DUO_SHOWDOWN;
    case BattleEventMode.TRIO_SHOWDOWN:
      return ModeIconSrc.TRIO_SHOWDOWN;
    case BattleEventMode.BASKET_BRAWL:
      return ModeIconSrc.BASKET_BRAWL;
    case BattleEventMode.BIG_GAME:
      return ModeIconSrc.BIG_GAME;
    case BattleEventMode.BOSS_FIGHT:
      return ModeIconSrc.BOSS_FIGHT;
    case BattleEventMode.BOT_DROP:    
      return ModeIconSrc.BOT_DROP;
    case BattleEventMode.BOUNTY:
      return ModeIconSrc.BOUNTY;
    case BattleEventMode.BRAWL_BALL:
      return ModeIconSrc.BRAWL_BALL;
    case BattleEventMode.BRAWL_BALL_5V5:
      return ModeIconSrc.BRAWL_BALL;
    case BattleEventMode.DUELS:
      return ModeIconSrc.DUELS;
    case BattleEventMode.GEM_GRAB:
      return ModeIconSrc.GEM_GRAB;
    case BattleEventMode.GEM_GRAB_5V5:
      return ModeIconSrc.GEM_GRAB;
    case BattleEventMode.HOT_ZONE:
      return ModeIconSrc.HOT_ZONE;
    case BattleEventMode.HUNTERS:
      return ModeIconSrc.HUNTERS;
    case BattleEventMode.HEIST:
      return ModeIconSrc.HEIST;
    case BattleEventMode.JELLYFISHING:
      return ModeIconSrc.JELLYFISHING;
    case BattleEventMode.KNOCKOUT:
      return ModeIconSrc.KNOCKOUT;
    case BattleEventMode.KNOCKOUT_5V5:
      return ModeIconSrc.KNOCKOUT;
    case BattleEventMode.PAINT_BRAWL:
      return null;
    case BattleEventMode.PAYLOAD:
      return ModeIconSrc.PAYLOAD;
    case BattleEventMode.PRESENT_PLUNDER:
      return ModeIconSrc.PRESENT_PLUNDER;
    case BattleEventMode.ROBO_RUMBLE:
      return ModeIconSrc.ROBO_RUMBLE;
    case BattleEventMode.TAKEDOWN:
      return ModeIconSrc.TAKEDOWN;
    case BattleEventMode.TROPHY_ESCAPE:
      return null;
    case BattleEventMode.VOLLEY_BRAWL:
      return ModeIconSrc.VOLLEY_BRAWL;
    case BattleEventMode.WIPEOUT:
      return ModeIconSrc.WIPEOUT;
    case BattleEventMode.WIPEOUT_5V5:
      return ModeIconSrc.WIPEOUT;
    case BattleEventMode.LONE_STAR:
      return ModeIconSrc.LONE_STAR;
    case BattleEventMode.SUPER_CITY_RAMPAGE:
      return ModeIconSrc.SUPER_CITY_RAMPAGE;
    case BattleEventMode.HOLD_THE_TROPHY:
      return ModeIconSrc.HOLD_THE_TROPHY;
    case BattleEventMode.TROPHY_THIEVES:
      return ModeIconSrc.TROPHY_THIEVES;
    case BattleEventMode.LAST_STAND:
      return ModeIconSrc.LAST_STAND;
    case BattleEventMode.SNOWTEL_THIEVES:
      return null;
    case BattleEventMode.PUMPKIN_PLUNDER:
      return null;
    case BattleEventMode.GODZILLA_CITY_SMASH:
      return ModeIconSrc.GODZILLA_CITY_SMASH;
    case BattleEventMode.ZOMBIE_PLUNDER:
      return null;
  }
}

const playerBattleIconSrc = (battle: PlayerBattle)=> {
  const eventModeIcon = battle.event ? battleEventModeIconSrc(battle.event.mode) : null;
  return eventModeIcon || battleModeIconSrc(battle.mode);
}
