import {BattleResult, BattleResultType} from "@/model/enums/BattleResult";
import {BattleType, BattleTypeType} from "@/model/enums/BattleType";
import {BattleEventMode, BattleEventModeType} from "@/model/enums/BattleEventMode";
import {BattleMode, BattleModeType} from "@/model/enums/BattleMode";
import { PlayerBattle } from "@/model/PlayerBattle";

export const battleResultTitle = (result: BattleResultType) => {
  switch (result) {
    case BattleResult.VICTORY:
      return '승리';
    case BattleResult.DEFEAT:
      return '패배';
    case BattleResult.DRAW:
      return '무승부';
  }
}

export const battleTypeTitle = (type: BattleTypeType) => {
  switch (type) {
    case BattleType.RANKED:
      return '트로피전';
    case BattleType.SOLO_RANKED:
      return '경쟁전';
    case BattleType.FRIENDLY:
      return '친선전';
    case BattleType.CHALLENGE:
      return '챌린지';
    default:
      return null;
  }
}

export const battleEventModeTitle = (mode: BattleEventModeType) => {
  switch (mode) {
    case BattleEventMode.NOT_FOUND:
      return '알 수 없음';
    case BattleEventMode.UNKNOWN:
      return '알 수 없음';
    case BattleEventMode.SOLO_SHOWDOWN:
      return '솔로 쇼다운';
    case BattleEventMode.DUO_SHOWDOWN:
      return '듀오 쇼다운';
    case BattleEventMode.TRIO_SHOWDOWN:
      return '트리오 쇼다운';
    case BattleEventMode.BASKET_BRAWL:
      return '바스켓 브롤';
    case BattleEventMode.BIG_GAME:
      return '빅 게임';
    case BattleEventMode.BOSS_FIGHT:
      return '보스전';
    case BattleEventMode.BOT_DROP:
      return '봇드롭';
    case BattleEventMode.BOUNTY:
      return '바운티';
    case BattleEventMode.BRAWL_BALL:
      return '브롤볼';
    case BattleEventMode.BRAWL_BALL_5V5:
      return '브롤볼 5VS5';
    case BattleEventMode.DUELS:
      return '듀얼';
    case BattleEventMode.GEM_GRAB:
      return '젬그랩';
    case BattleEventMode.GEM_GRAB_5V5:
      return '젬그랩 5VS5';
    case BattleEventMode.HOT_ZONE:
      return '핫존';
    case BattleEventMode.HUNTERS:
      return '헌터즈';
    case BattleEventMode.HEIST:
      return '하이스트';
    case BattleEventMode.JELLYFISHING:
      return '해파리사냥'
    case BattleEventMode.KNOCKOUT:
      return '녹아웃';
    case BattleEventMode.KNOCKOUT_5V5:
      return '녹아웃 5VS5';
    case BattleEventMode.PAINT_BRAWL:
      return '페인트 브롤';
    case BattleEventMode.PAYLOAD:
      return '페이로드';
    case BattleEventMode.PRESENT_PLUNDER:
      return '선물 훔치기';
    case BattleEventMode.ROBO_RUMBLE:
      return '로보럼블';
    case BattleEventMode.TAKEDOWN:
      return '테이크다운';
    case BattleEventMode.TROPHY_ESCAPE:
      return '트로피 탈출';
    case BattleEventMode.VOLLEY_BRAWL:
      return '발리 브롤';
    case BattleEventMode.WIPEOUT:
      return '클린 아웃';
    case BattleEventMode.WIPEOUT_5V5:
      return '클린 아웃 5VS5';
    case BattleEventMode.LONE_STAR:
      return '론스타';
    case BattleEventMode.SUPER_CITY_RAMPAGE:
      return '슈퍼시티 램피지';
    case BattleEventMode.HOLD_THE_TROPHY:
      return '트로피를 잡아라'
    case BattleEventMode.TROPHY_THIEVES:
      return '트로피 도둑';
    case BattleEventMode.LAST_STAND:
      return '최후의 저항';
    case BattleEventMode.SNOWTEL_THIEVES:
      return '스노텔 도둑';
    case BattleEventMode.PUMPKIN_PLUNDER:
      return '호박 훔치기';
    case BattleEventMode.GODZILLA_CITY_SMASH:
      return '고질라 시티 스매시';
    case BattleEventMode.ZOMBIE_PLUNDER:
      return '좀비 훔치기';
  }
}

export const battleModeTitle = (mode: BattleModeType) => {
  switch (mode) {
    case BattleMode.NOT_FOUND:
      return '알 수 없음';
    case BattleMode.KNOCKOUT:
      return '녹아웃';
    case BattleMode.GEM_GRAB:
      return '젬그랩';
    case BattleMode.HEIST:
      return '하이스트';
    case BattleMode.HOT_ZONE:
      return '핫존';
    case BattleMode.SIEGE:
      return '시즈';
    case BattleMode.BOUNTY:
      return '바운티';
    case BattleMode.BRAWL_BALL:
      return '브롤볼';
    case BattleMode.DUELS:
      return '듀얼';
    case BattleMode.SOLO_SHOWDOWN:
      return '솔로 쇼다운';
    case BattleMode.DUO_SHOWDOWN:
      return '듀오 쇼다운';
    case BattleMode.WIPEOUT:
      return '클린 아웃';
    case BattleMode.VOLLEY_BRAWL:
      return '발리 브롤';
    case BattleMode.TROPHY_THIEVES:
      return '트로피 도둑';
    case BattleMode.BOSS_FIGHT:
      return '보스전';
    case BattleMode.BIG_GAME:
      return '빅 게임';
    case BattleMode.BASKET_BRAWL:
      return '바스켓 브롤';
    case BattleMode.ROBO_RUMBLE:
      return '로보럼블';
    case BattleMode.PAYLOAD:
      return '페이로드';
    case BattleMode.TAKEDOWN:
      return '테이크다운';
    case BattleMode.HUNTERS:
      return '헌터즈';
    case BattleMode.BOT_DROP:
      return '봇드롭';
    case BattleMode.HOLD_THE_TROPHY:
      return '트로피를 잡아라';
  }
}

export const playerBattleModeTitle = (battle: PlayerBattle) => {
  if (battle.event) {
    return battleEventModeTitle(battle.event.mode);
  }

  return battleModeTitle(battle.mode);
}
