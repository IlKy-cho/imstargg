import {BattleResultValue, BattleResult} from "@/model/enums/BattleResult";
import {BattleTypeValue, BattleType} from "@/model/enums/BattleType";
import {BattleEventModeValue, BattleEventMode} from "@/model/enums/BattleEventMode";
import {BattleModeValue, BattleMode} from "@/model/enums/BattleMode";
import { PlayerBattle } from "@/model/PlayerBattle";

export const battleResultTitle = (result: BattleResult) => {
  switch (result) {
    case BattleResultValue.VICTORY:
      return '승리';
    case BattleResultValue.DEFEAT:
      return '패배';
    case BattleResultValue.DRAW:
      return '무승부';
  }
}

export const battleEventModeTitle = (mode: BattleEventMode) => {
  switch (mode) {
    case BattleEventModeValue.NOT_FOUND:
      return null;
    case BattleEventModeValue.UNKNOWN:
      return null;
    case BattleEventModeValue.SOLO_SHOWDOWN:
      return '솔로 쇼다운';
    case BattleEventModeValue.DUO_SHOWDOWN:
      return '듀오 쇼다운';
    case BattleEventModeValue.TRIO_SHOWDOWN:
      return '트리오 쇼다운';
    case BattleEventModeValue.BASKET_BRAWL:
      return '바스켓 브롤';
    case BattleEventModeValue.BIG_GAME:
      return '빅 게임';
    case BattleEventModeValue.BOSS_FIGHT:
      return '보스전';
    case BattleEventModeValue.BOT_DROP:
      return '봇드롭';
    case BattleEventModeValue.BOUNTY:
      return '바운티';
    case BattleEventModeValue.BRAWL_BALL:
      return '브롤볼';
    case BattleEventModeValue.BRAWL_BALL_5V5:
      return '브롤볼 5VS5';
    case BattleEventModeValue.DUELS:
      return '듀얼';
    case BattleEventModeValue.GEM_GRAB:
      return '젬그랩';
    case BattleEventModeValue.GEM_GRAB_5V5:
      return '젬그랩 5VS5';
    case BattleEventModeValue.HOT_ZONE:
      return '핫존';
    case BattleEventModeValue.HUNTERS:
      return '헌터즈';
    case BattleEventModeValue.HEIST:
      return '하이스트';
    case BattleEventModeValue.JELLYFISHING:
      return '해파리사냥'
    case BattleEventModeValue.KNOCKOUT:
      return '녹아웃';
    case BattleEventModeValue.KNOCKOUT_5V5:
      return '녹아웃 5VS5';
    case BattleEventModeValue.PAINT_BRAWL:
      return '페인트 브롤';
    case BattleEventModeValue.PAYLOAD:
      return '페이로드';
    case BattleEventModeValue.PRESENT_PLUNDER:
      return '선물 훔치기';
    case BattleEventModeValue.ROBO_RUMBLE:
      return '로보럼블';
    case BattleEventModeValue.TAKEDOWN:
      return '테이크다운';
    case BattleEventModeValue.TROPHY_ESCAPE:
      return '트로피 탈출';
    case BattleEventModeValue.VOLLEY_BRAWL:
      return '발리 브롤';
    case BattleEventModeValue.WIPEOUT:
      return '클린 아웃';
    case BattleEventModeValue.WIPEOUT_5V5:
      return '클린 아웃 5VS5';
    case BattleEventModeValue.LONE_STAR:
      return '론스타';
    case BattleEventModeValue.SUPER_CITY_RAMPAGE:
      return '슈퍼시티 램피지';
    case BattleEventModeValue.HOLD_THE_TROPHY:
      return '트로피를 잡아라'
    case BattleEventModeValue.TROPHY_THIEVES:
      return '트로피 도둑';
    case BattleEventModeValue.LAST_STAND:
      return '최후의 저항';
    case BattleEventModeValue.SNOWTEL_THIEVES:
      return '스노텔 도둑';
    case BattleEventModeValue.PUMPKIN_PLUNDER:
      return '호박 훔치기';
    case BattleEventModeValue.GODZILLA_CITY_SMASH:
      return '고질라 시티 스매시';
    case BattleEventModeValue.ZOMBIE_PLUNDER:
      return '좀비 훔치기';
  }
}

export const battleModeTitle = (mode: BattleMode) => {
  switch (mode) {
    case BattleModeValue.NOT_FOUND:
      return null;
    case BattleModeValue.KNOCKOUT:
      return '녹아웃';
    case BattleModeValue.GEM_GRAB:
      return '젬그랩';
    case BattleModeValue.HEIST:
      return '하이스트';
    case BattleModeValue.HOT_ZONE:
      return '핫존';
    case BattleModeValue.SIEGE:
      return '시즈';
    case BattleModeValue.BOUNTY:
      return '바운티';
    case BattleModeValue.BRAWL_BALL:
      return '브롤볼';
    case BattleModeValue.DUELS:
      return '듀얼';
    case BattleModeValue.SOLO_SHOWDOWN:
      return '솔로 쇼다운';
    case BattleModeValue.DUO_SHOWDOWN:
      return '듀오 쇼다운';
    case BattleModeValue.WIPEOUT:
      return '클린 아웃';
    case BattleModeValue.VOLLEY_BRAWL:
      return '발리 브롤';
    case BattleModeValue.TROPHY_THIEVES:
      return '트로피 도둑';
    case BattleModeValue.BOSS_FIGHT:
      return '보스전';
    case BattleModeValue.BIG_GAME:
      return '빅 게임';
    case BattleModeValue.BASKET_BRAWL:
      return '바스켓 브롤';
    case BattleModeValue.ROBO_RUMBLE:
      return '로보럼블';
    case BattleModeValue.PAYLOAD:
      return '페이로드';
    case BattleModeValue.TAKEDOWN:
      return '테이크다운';
    case BattleModeValue.HUNTERS:
      return '헌터즈';
    case BattleModeValue.BOT_DROP:
      return '봇드롭';
    case BattleModeValue.HOLD_THE_TROPHY:
      return '트로피를 잡아라';
  }
}

export const playerBattleModeTitle = (battle: PlayerBattle) => {
  if (battle.event.mode) {
    return battleEventModeTitle(battle.event.mode);
  }

  return battleModeTitle(battle.mode);
}
